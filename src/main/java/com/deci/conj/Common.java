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
	private Common() {
	}

	static void async(Runnable runnable) {
		new Thread(runnable).start();
	}

	@SneakyThrows
	static void download(String url, String name) {
		URL website = new URL(url);
		ReadableByteChannel rbc = Channels.newChannel(website.openStream());
		FileOutputStream fos = new FileOutputStream(name);
		fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
	}

	static void printArray(Object[] obj) {
		for (Object o : obj) {
			System.out.printf("%s ", o);
		}
		System.out.println();
	}

	@SneakyThrows
	static void exec(String cmd) {
		final Process p = Runtime.getRuntime().exec(cmd);
		System.out.printf("Executing %s\n", cmd);
		new Thread(() -> {
			BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = null;

			try {
				while ((line = input.readLine()) != null)
					System.out.println(line);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}).start();

		p.waitFor();
	}
}
