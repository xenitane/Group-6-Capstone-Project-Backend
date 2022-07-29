package ola.training.group6.wallStreet.util;

public class Generator {
	public static String idGen() {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < 8; i++) stringBuilder.append(Integer.toHexString((int) (Math.random() * 16)));
		stringBuilder.append('-');
		for (int i = 0; i < 4; i++) stringBuilder.append(Integer.toHexString((int) (Math.random() * 16)));
		stringBuilder.append('-');
		for (int i = 0; i < 4; i++) stringBuilder.append(Integer.toHexString((int) (Math.random() * 16)));
		stringBuilder.append('-');
		for (int i = 0; i < 4; i++) stringBuilder.append(Integer.toHexString((int) (Math.random() * 16)));
		stringBuilder.append('-');
		for (int i = 0; i < 12; i++) stringBuilder.append(Integer.toHexString((int) (Math.random() * 16)));
		return stringBuilder.toString();
	}
}
