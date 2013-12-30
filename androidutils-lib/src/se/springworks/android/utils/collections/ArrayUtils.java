package se.springworks.android.utils.collections;

import java.util.ArrayList;
import java.util.List;

public class ArrayUtils {
	
	public static final Integer[] EMPTY_INTEGER_OBJECT_ARRAY = {};
	public static final Byte[] EMPTY_BYTE_OBJECT_ARRAY = {};
	public static final Short[] EMPTY_SHORT_OBJECT_ARRAY = {};
	public static final Float[] EMPTY_FLOAT_OBJECT_ARRAY = {};
	public static final Double[] EMPTY_DOUBLE_OBJECT_ARRAY = {};
	public static final Long[] EMPTY_LONG_OBJECT_ARRAY = {};
	public static final Character[] EMPTY_CHAR_OBJECT_ARRAY = {};
	public static final Boolean[] EMPTY_BOOLEAN_OBJECT_ARRAY = {};

	public static List<Integer> asList(int[] a) {
		ArrayList<Integer> list = new ArrayList<Integer>(a.length);
		for(int v : a) {
			list.add(v);
		}
		return list;
	}

	public static List<Long> asList(long[] a) {
		ArrayList<Long> list = new ArrayList<Long>(a.length);
		for(long v : a) {
			list.add(v);
		}
		return list;
	}

	public static List<Boolean> asList(boolean[] a) {
		ArrayList<Boolean> list = new ArrayList<Boolean>(a.length);
		for(boolean v : a) {
			list.add(v);
		}
		return list;
	}

	public static List<Short> asList(short[] a) {
		ArrayList<Short> list = new ArrayList<Short>(a.length);
		for(short v : a) {
			list.add(v);
		}
		return list;
	}

	public static List<Byte> asList(byte[] a) {
		ArrayList<Byte> list = new ArrayList<Byte>(a.length);
		for(byte v : a) {
			list.add(v);
		}
		return list;
	}

	public static List<Float> asList(float[] a) {
		ArrayList<Float> list = new ArrayList<Float>(a.length);
		for(float v : a) {
			list.add(v);
		}
		return list;
	}

	public static List<Double> asList(double[] a) {
		ArrayList<Double> list = new ArrayList<Double>(a.length);
		for(double v : a) {
			list.add(v);
		}
		return list;
	}

	public static List<Character> asList(char[] a) {
		ArrayList<Character> list = new ArrayList<Character>(a.length);
		for(char v : a) {
			list.add(v);
		}
		return list;
	}
	
	
	public static int[] toPrimitiveArray(Integer[] objects) {
		int[] a = new int[objects.length];
		for(int i = 0; i < a.length; i++) {
			a[i] = objects[i];
		}
		return a;
	}
	
	public static long[] toPrimitiveArray(Long[] objects) {
		long[] a = new long[objects.length];
		for(int i = 0; i < a.length; i++) {
			a[i] = objects[i];
		}
		return a;
	}
	
	public static short[] toPrimitiveArray(Short[] objects) {
		short[] a = new short[objects.length];
		for(int i = 0; i < a.length; i++) {
			a[i] = objects[i];
		}
		return a;
	}

	
	public static byte[] toPrimitiveArray(Byte[] objects) {
		byte[] a = new byte[objects.length];
		for(int i = 0; i < a.length; i++) {
			a[i] = objects[i];
		}
		return a;
	}
	
	public static boolean[] toPrimitiveArray(Boolean[] objects) {
		boolean[] a = new boolean[objects.length];
		for(int i = 0; i < a.length; i++) {
			a[i] = objects[i];
		}
		return a;
	}
	
	public static float[] toPrimitiveArray(Float[] objects) {
		float[] a = new float[objects.length];
		for(int i = 0; i < a.length; i++) {
			a[i] = objects[i];
		}
		return a;
	}
	
	public static double[] toPrimitiveArray(Double[] objects) {
		double[] a = new double[objects.length];
		for(int i = 0; i < a.length; i++) {
			a[i] = objects[i];
		}
		return a;
	}
	
	public static char[] toPrimitiveArray(Character[] objects) {
		char[] a = new char[objects.length];
		for(int i = 0; i < a.length; i++) {
			a[i] = objects[i];
		}
		return a;
	}
	
	
	public static Integer[] toObjectArray(int[] primitives) {
		Integer[] a = new Integer[primitives.length];
		for(int i = 0; i < a.length; i++) {
			a[i] = primitives[i];
		}
		return a;
	}
	
	public static Long[] toObjectArray(long[] primitives) {
		Long[] a = new Long[primitives.length];
		for(int i = 0; i < a.length; i++) {
			a[i] = primitives[i];
		}
		return a;
	}
	
	public static Short[] toObjectArray(short[] primitives) {
		Short[] a = new Short[primitives.length];
		for(int i = 0; i < a.length; i++) {
			a[i] = primitives[i];
		}
		return a;
	}

	
	public static Byte[] toObjectArray(byte[] primitives) {
		Byte[] a = new Byte[primitives.length];
		for(int i = 0; i < a.length; i++) {
			a[i] = primitives[i];
		}
		return a;
	}
	
	public static Boolean[] toObjectArray(boolean[] primitives) {
		Boolean[] a = new Boolean[primitives.length];
		for(int i = 0; i < a.length; i++) {
			a[i] = primitives[i];
		}
		return a;
	}
	
	public static Float[] toObjectArray(float[] primitives) {
		Float[] a = new Float[primitives.length];
		for(int i = 0; i < a.length; i++) {
			a[i] = primitives[i];
		}
		return a;
	}
	
	public static Double[] toObjectArray(double[] primitives) {
		Double[] a = new Double[primitives.length];
		for(int i = 0; i < a.length; i++) {
			a[i] = primitives[i];
		}
		return a;
	}
	
	public static Character[] toObjectArray(char[] primitives) {
		Character[] a = new Character[primitives.length];
		for(int i = 0; i < a.length; i++) {
			a[i] = primitives[i];
		}
		return a;
	}

}
