package com.deci.conj;

import com.deci.razorcommon.Common;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

//todo
@AllArgsConstructor
public class WebVerb {

	@Getter
	private String infinitive;
	@Getter
	private String stemChange;
	@Getter
	private String yo;
	@Getter
	private String subjunctiveStem;
	@Getter
	private String preteriteStem;
	@Getter
	private String futureStem;

	private WebVerb() {

	}

	@SneakyThrows
	public static List<WebVerb> fromMemory(byte[] b) {

		ByteBuffer bb = ByteBuffer.wrap(b);
		//Common.readBytes(bb, 4); // garbage memory

		var arr = new ArrayList<WebVerb>();

		while (bb.remaining() > 0) {
			WebVerb lr = new WebVerb();

			lr.infinitive = new String(Common.readBytes(bb, bb.get()), "UTF-8");
			lr.stemChange = new String(Common.readBytes(bb, bb.get()), "UTF-8");
			lr.yo = new String(Common.readBytes(bb, bb.get()), "UTF-8");
			lr.subjunctiveStem = new String(Common.readBytes(bb, bb.get()), "UTF-8");
			lr.preteriteStem = new String(Common.readBytes(bb, bb.get()), "UTF-8");
			lr.futureStem = new String(Common.readBytes(bb, bb.get()), "UTF-8");
			arr.add(lr);
		}

		return arr;
	}

	private static void aggregate(List<Byte> ls, byte[] mem) {
		for (byte b : mem) {
			ls.add(b);
		}
	}

	@SneakyThrows
	public byte[] serialize() {
		var mem = new ArrayList<Byte>();

		var inf_mem = infinitive.getBytes("UTF-8");
		mem.add((byte) inf_mem.length);
		aggregate(mem, inf_mem);

		var stem_mem = stemChange.getBytes("UTF-8");
		mem.add((byte) stem_mem.length);
		aggregate(mem, stem_mem);

		var yo_mem = yo.getBytes("UTF-8");
		mem.add((byte) yo_mem.length);
		aggregate(mem, yo_mem);

		var subj_mem = subjunctiveStem.getBytes("UTF-8");
		mem.add((byte) subj_mem.length);
		aggregate(mem, subj_mem);

		var pret_mem = preteriteStem.getBytes("UTF-8");
		mem.add((byte) pret_mem.length);
		aggregate(mem, pret_mem);

		var fut_mem = futureStem.getBytes("UTF-8");
		mem.add((byte) fut_mem.length);
		aggregate(mem, fut_mem);
		Common.printArray(mem.toArray());
		return Common.asPrimitive(mem);
	}

	@Override
	public String toString() {
		return String.format("%s | %s | %s | %s | %s | %s", infinitive, stemChange, yo, subjunctiveStem, preteriteStem, futureStem);
	}
}
