package com.esporter.server.model.database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

import com.esporter.both.types.TypesStable;

import oracle.jdbc.pool.OracleDataSource;


public class DatabaseAccess {

	private static DatabaseAccess instance;
	private Connection conn;
	private QueueDatabase<Query> in;
	private QueueDatabase<Result> out;
	private Thread t;

	private boolean run = true;
	private Timer timerCheckAlive;
	private OracleDataSource dataSource;
	
	private DatabaseAccess() throws SQLException {
		in = new QueueDatabase<>(this);
		out = new QueueDatabase<>(this);
		connexion();
		DatabaseAccess database = this;	
		
		
		
	
		
		TimerTask tt = new TimerTask() {
			
			@Override
			public void run() {
				pingConnexion();
				
			}
		};
		
		//Ping the db to know if the connection is stil active
		timerCheckAlive = new Timer();
		timerCheckAlive.scheduleAtFixedRate(tt, 120000, 120000);
		
		
		
	    
	    
		t = new Thread( () -> {
				while(run) {
					synchronized (database) {

						if (in.getNbElement()==0)
							try {
								database.wait();
							} catch (InterruptedException e) {
								Thread.currentThread().interrupt();
							}
						Entry<Integer, Query> entry = in.next();
						
						int  id = entry.getKey();
						Query r = entry.getValue();
						Result rs=new Result(null, 0, false);
						switch (r.getType()) {
						case FUNCTION:
							CallableStatement cstmt = null;
							try {
								cstmt = conn.prepareCall(r.getQuery());
								cstmt.registerOutParameter(1, Types.INTEGER);
								if (r.getDates()!=null) {
									for (int i=2;i<r.getDates().length+2;i++) {
										cstmt.setTimestamp(i, r.getDates()[i-2]);
									}
								}
								cstmt.executeUpdate();

								int integer = cstmt.getInt(1);
								rs.setInteger(integer);
							} catch (SQLException e1) {
								rs.setError(true);
							}
							
							out.put(rs, id);
							
							break;
						case PROCEDURE:
							CallableStatement cstmt2 = null;
							try {
								cstmt2 = conn.prepareCall(r.getQuery());
								synchronized (instance) {
									cstmt2.executeUpdate();
								}
							} catch (SQLException e2) {
								rs.setError(true);
							}

							out.put(rs, id);
							
							break;
						case QUERY:
							
							Statement st = null;
							try {
								st = conn.createStatement();
								synchronized (instance) {
									rs.setResultSet(st.executeQuery(r.getQuery()));
								}
							} catch (SQLException e1) {
								rs.setError(true);
							}

							out.put(rs, id);

							break;
						case INSERT:
							Statement st1 = null;
							try {
								st1 = conn.createStatement();
								synchronized (instance) {
									rs.setResultSet(st1.executeQuery(r.getQuery()));
								}
							} catch (SQLException e1) {
								rs.setError(true);
							}finally {
								try {
									st1.close();
								} catch (SQLException e) {
								}
							}
							break;
						case INSERT_DATE_BLOB:
							CallableStatement insert = null;
							try {
								insert = conn.prepareCall(r.getQuery());
								insert.registerOutParameter(1, Types.INTEGER);
								insert.setBinaryStream(2, r.getInputStream());
								int j = 3;
								if (r.getDates()!=null) {
									for (int i=0;i<r.getDates().length;i++) {
										insert.setTimestamp(i+j, r.getDates()[i]);
									}
								}
								synchronized (instance) {
									insert.executeUpdate();
								}
								int entier = insert.getInt(1);
								rs.setInteger(entier);
							} catch (SQLException e1) {
								rs.setError(true);
							} finally {
								try {
									insert.close();
								} catch (SQLException e) {
								}
							}

							out.put(rs, id);

						break;
					case MODIFY_DATE_BLOB:
						CallableStatement modify = null;
						try {
							modify = conn.prepareCall(r.getQuery());
							modify.setBinaryStream(1, r.getInputStream());
							int j = 2;
							if (r.getDates()!=null) {
								for (int i=0;i<r.getDates().length;i++) {
									modify.setTimestamp(i+j, r.getDates()[i]);
								}
							}
							synchronized (instance) {
								modify.executeUpdate();
							}
						} catch (SQLException e1) {
							rs.setError(true);
						} finally {
							try {
								modify.close();
							} catch (SQLException e) {
							}
						}

						out.put(rs, id);

							
						}
					}
				}
				
		});
		t.setDaemon(true);
		t.start();
		
	}
	
	
	
	
	private void pingConnexion() {
		synchronized (instance) {
			try {
				if(!this.conn.isValid(10)) {
					System.out.println("Reconnecting");
					reconnexion();
				} else {
					System.out.println("DB is alive");
				}
			} catch (SQLException e) {
			}
		}
	}
	
	private void connexion() throws SQLException {
		dataSource = new OracleDataSource();
		dataSource.setURL("jdbc:oracle:thin:@telline.univ-tlse3.fr:1521:etupre");
		dataSource.setUser("MRC4302A");
		dataSource.setPassword("$iutinfo");
		conn = dataSource.getConnection();
        System.out.println("Connexion OK");

    }
	
	private void reconnexion() throws SQLException {
		conn = dataSource.getConnection();
        System.out.println("Reconnexion OK");

    }
	
	public static DatabaseAccess getInstance() throws SQLException {
		if (instance==null)
			instance = new DatabaseAccess();
		return instance;
	}
	
	public Result getData(Query query){
		int id = in.put(query);
		Entry<Integer, Result> data;
		data = out.get(id);
		
		return data.getValue();
	}
	
	public Result insertData(Query query) throws InterruptedException {
		int id = in.put(query);
		Entry<Integer, Result> data;
		data = out.get(id);
		
		return data.getValue();
		
	}
	

	
	public Result login(Query query) throws InterruptedException {
		int id = in.put(query);
		Entry<Integer, Result> data;
		data = out.get(id);
		System.out.println(data.getValue().getInteger());
		return data.getValue();
	}
	
	public TypesStable getEcurie(Query query) throws InterruptedException {
		TypesStable ecurie = new TypesStable(null, null, null, 0);
		int id = in.put(query);
		Entry<Integer, Result> data;
		data = out.get(id);
		return ecurie;
	}
	
	public Thread getT() {
		return t;
	}
	
	
	public Timer getTimerCheckAlive() {
		return timerCheckAlive;
	}
	
	public Connection getConn() {
		return conn;
	}
	
	public void stopThread() throws InterruptedException {
		this.run = false;
		this.t.join();
	}
	
	
}
