package com.esporter.client.test;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Scanner;

import com.esporter.both.types.TypesPermission;
import com.esporter.both.types.exception.ExceptionLogin;
import com.esporter.client.model.user.User;

public class test {

	public static void main(String[] args) throws UnknownHostException, IOException, ExceptionLogin {
		User u = new User();
		u.login("test", "mdpTest");
		System.out.println(u.getPermission());
		System.out.println(u.getInfo());

	}

}
