package com.deci.conj;

import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

class Common {
	private static final String ANSI_BOLD  = "\033[0;1m";
	private static final String ANSI_CLOSE = "\033[0;0m";

	private Common() {
	}

	static void async(Runnable runnable) {
		new Thread(runnable).start();
	}

	static String align(String s, int align) {
		return s + Common.repeatStr(' ', align - s.length());
	}

	static String[] align(String[] arr, int align) {
		// +12 chars for the ANSI sequences
		String[] out = new String[arr.length];
		int i = 0;
		for (String s : arr) {

			out[i++] = align(s, align);
		}
		return out;
	}

	static String boldStr(String s) {
		return ANSI_BOLD + s + ANSI_CLOSE;
	}

	static String repeatStr(char c, int i) {
		StringBuilder sb = new StringBuilder();
		for (int x = 0; x < i; x++)
			sb.append(c);
		return sb.toString();
	}

	@SneakyThrows
	static void download(String url, String name) {
		URL website = new URL(url);
		ReadableByteChannel rbc = Channels.newChannel(website.openStream());
		FileOutputStream fos = new FileOutputStream(name);
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
	}

	static void printArray(Object[] obj) {
		for (int i = 0; i < obj.length; i++) {
			if (i + 1 != obj.length) {
				System.out.printf("%s, ", obj[i]);
			} else {
				System.out.printf("%s ", obj[i]);
			}
		}

		System.out.println();
	}

	static String[] toStringArray(Object[] obj) {
		String[] arr = new String[obj.length];
		for (int i = 0; i < arr.length; i++)
			arr[i] = obj[i].toString();
		return arr;
	}

	@SneakyThrows
	static void exec(String cmd) {
		final Process p = Runtime.getRuntime().exec(cmd);
		System.out.printf("Executing %s\n", cmd);
		Thread t = new Thread(() -> {
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = null;

			try {
				while ((line = input.readLine()) != null)
					System.out.println(line);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		t.start();
		t.join();

		p.waitFor();
	}
}
