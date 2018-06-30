package com.deci.conj;

import com.deci.razorcommon.RazorLogger;
import lombok.SneakyThrows;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;

public final class Serializer {
	private static final File DAT_FILE = new File(System.getenv("userprofile") + "\\Desktop\\MEM.dat");

	public static WebVerb readWebVerb(String infinitive) {
		for (var v : read()) {
			if (v.getInfinitive().equals(infinitive)) {
				return v;
			}
		}
		return null;
	}
	public static final String TAG_SERIALIZER = "Serializer";

	@SneakyThrows
	public static List<WebVerb> read() {

		final byte[] mem = Files.readAllBytes(DAT_FILE.toPath());
		var ls = WebVerb.fromMemory(mem);
		RazorLogger.out(TAG_SERIALIZER,"Read %d objects", ls.size());
		return ls;
	}

	@SneakyThrows
	public static void write(List<WebVerb> verbs) {
		for (WebVerb v : verbs) {
			Files.write(DAT_FILE.toPath(), v.serialize(), StandardOpenOption.APPEND);
			//OBJECT_OUT.write(v.serialize());
		}
	}
}
