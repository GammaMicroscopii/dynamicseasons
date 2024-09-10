package com.gammamicroscopii.world;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Pair;
import net.minecraft.util.Util;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.math.ChunkPos;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.util.HashMap;

import static com.gammamicroscopii.world.ServerWorldTick.server;

public class ModChunkDataEncoding {

	//TODO: DATAFIX (on next version)
	//TODO: MAKE SO THAT ONLY THERE'S ONE RANDOMACCESSFILE PER FILE
	//TODO: FIND A WAY TO TAKE INTO ACCOUNT THE BYTE OFFSET OF EACH FIELD ONCE THERE ARE MULTIPLE

	/**
	 * Number changed with every mod version that changes data format for regions
	 */
	public static final int CURRENT_MOD_CHUNK_DATA_VERSION = 1;

	/**
	 * Contains the encoding format for region files of all known data versions
	 * Objects:
	 * <ul>
	 *   <li>Integer: each data version</li>
	 *   <li>Pair[]: all the fields each chunk has on that version</br>
	 *   	<ul>
	 *     	<li>String: a key name for each field</li>
	 *     	<li>Integer: the number of bytes the field takes up in the file</li>
	 *   	</ul>
	 *   </li>
	 * </ul>
	 */
	public static final HashMap<Integer, ChunkDataField[]> DATA_VERSION_ENCODINGS = Util.make(new HashMap<>(), (map) -> {
		map.put(1, new ChunkDataField[]{
						new ChunkDataField("last_unloaded_time", FieldType.LONG, Long.MIN_VALUE)
		});
	});

	public static ChunkDataField[] getEncodingForVersion(int dataVersion) {
		return DATA_VERSION_ENCODINGS.get(dataVersion);
	}

	public static ChunkDataField[] getEncodingForVersionOrThrow(int dataVersion) throws UnknownDataVersionException {
		ChunkDataField[] ret = getEncodingForVersion(dataVersion);
		if (ret == null) throw new UnknownDataVersionException("Unknown data version: " + dataVersion);
		return ret;
	}

	public static int getChunkEntrySizeBytes(ChunkDataField[] cdf) {
		int ret = 0;
		for (int i = 0; i < cdf.length; i++) {
			ret += cdf[i].type().bytes;
		}
		return ret;
	}

	public static Path getPathToDimension(ServerWorld dimension) {
		return server.getSavePath(WorldSavePath.ROOT).resolve("dynamicseasons_regions").resolve(dimension.getRegistryKey().getValue().toString());
	}

	public static File createDataFileIfNotExists(File file) {

		if (!file.exists()) {
			try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
				raf.writeInt(CURRENT_MOD_CHUNK_DATA_VERSION);
				ChunkDataField[] enc = getEncodingForVersion(CURRENT_MOD_CHUNK_DATA_VERSION);
				for (int i = 0; i < 512; i++) {
					for (int j = 0; j < enc.length; j++) {
						switch (enc[j].type()) {
							case BYTE -> raf.writeByte(enc[j].defaultValue().intValue());
							case SHORT -> raf.writeShort(enc[j].defaultValue().intValue());
							case INT -> raf.writeInt(enc[j].defaultValue().intValue());
							case LONG -> raf.writeLong(enc[j].defaultValue().longValue());
							case FLOAT -> raf.writeFloat(enc[j].defaultValue().floatValue());
							case DOUBLE -> raf.writeDouble(enc[j].defaultValue().byteValue());
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
				int fileVersion = raf.readInt();
				if (fileVersion != CURRENT_MOD_CHUNK_DATA_VERSION) {
					datafixFile(file, fileVersion);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return file;
	}

	/**
	 * No need to implement this until version 2.
	 *
	 * @param file
	 * @param fileVersion the version found on the file
	 */
	private static void datafixFile(File file, int fileVersion) {
		throw new RuntimeException("Not implemented");
	}

	public static String getRegionFileName(ChunkPos chunkPos) {
		return "r." + chunkPos.getRegionX() + "." + chunkPos.getRegionZ();
	}

	public static int getRegionRelativeChunkIndex(ChunkPos chunkPos) {
		return chunkPos.getRegionRelativeX() << 5 + chunkPos.getRegionRelativeZ();
	}

	public static int getFilePointerPosition(ChunkPos chunkPos) { //ONLY FOR THE FIRST (and now the only one) FIELD
		return 4 /*data version int*/ + getChunkEntrySizeBytes(getEncodingForVersion(CURRENT_MOD_CHUNK_DATA_VERSION)) * getRegionRelativeChunkIndex(chunkPos);
	}

	public static void setLastUnloadedTime(ServerWorld serverWorld, ChunkPos chunkPos) {
		File regionDataFile = createDataFileIfNotExists(getPathToDimension(serverWorld).resolve(getRegionFileName(chunkPos)).toFile());
		try (RandomAccessFile raf = new RandomAccessFile(regionDataFile, "rw")) {
			raf.seek(getFilePointerPosition(chunkPos) /* + OFFSET*/);
			raf.writeLong(serverWorld.getTimeOfDay());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static long getLastUnloadedTime(ServerWorld serverWorld, ChunkPos chunkPos) {
		File regionDataFile = createDataFileIfNotExists(getPathToDimension(serverWorld).resolve(getRegionFileName(chunkPos)).toFile());
		try (RandomAccessFile raf = new RandomAccessFile(regionDataFile, "r")) {
			raf.seek(getFilePointerPosition(chunkPos) /* + OFFSET*/);
			return raf.readLong();
		} catch (IOException e) {
			e.printStackTrace();
		}
		throw new RuntimeException("something weird is going on");
	}

}

class UnknownDataVersionException extends Exception {

	public UnknownDataVersionException(String msg) {
		super(msg);
	}
}

record ChunkDataField(String key, FieldType type, Number defaultValue) {
}

enum FieldType {
	BYTE(1),
	SHORT(2),
	INT(4),
	LONG(8),
	FLOAT(4),
	DOUBLE(8);

	final int bytes;

	FieldType(int bytes) {
		this.bytes = bytes;
	}
}
