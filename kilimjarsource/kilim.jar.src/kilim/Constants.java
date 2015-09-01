package kilim;

import org.objectweb.asm.Opcodes;

public abstract interface Constants
  extends Opcodes
{
  public static final String KILIM_VERSION = "0.7.3";
  public static final String D_BOOLEAN = "Z";
  public static final String D_BYTE = "B";
  public static final String D_CHAR = "C";
  public static final String D_DOUBLE = "D";
  public static final String D_FLOAT = "F";
  public static final String D_INT = "I";
  public static final String D_LONG = "J";
  public static final String D_SHORT = "S";
  public static final String D_VOID = "V";
  public static final String D_ARRAY_BOOLEAN = "[Z";
  public static final String D_ARRAY_BYTE = "[B";
  public static final String D_ARRAY_CHAR = "[C";
  public static final String D_ARRAY_DOUBLE = "[D";
  public static final String D_ARRAY_FLOAT = "[F";
  public static final String D_ARRAY_SHORT = "[S";
  public static final String D_ARRAY_INT = "[I";
  public static final String D_ARRAY_LONG = "[J";
  public static final String D_NULL = "NULL";
  public static final String D_RETURN_ADDRESS = "A";
  public static final String D_OBJECT = "Ljava/lang/Object;";
  public static final String D_STRING = "Ljava/lang/String;";
  public static final String D_THROWABLE = "Ljava/lang/Throwable;";
  public static final String D_UNDEFINED = "UNDEFINED";
  public static final String D_FIBER = "Lkilim/Fiber;";
  public static final String D_STATE = "Lkilim/State;";
  public static final String D_TASK = "Lkilim/Task;";
  public static final String D_PAUSABLE = "Lkilim/Pausable;";
  public static final String THROWABLE_CLASS = "java/lang/Throwable";
  public static final String FIBER_CLASS = "kilim/Fiber";
  public static final String STATE_CLASS = "kilim/State";
  public static final String TASK_CLASS = "kilim/Task";
  public static final String PAUSABLE_CLASS = "kilim/Pausable";
  public static final String NOT_PAUSABLE_CLASS = "kilim/NotPausable";
  public static final String WOVEN_FIELD = "$isWoven";
  public static final int ILOAD_0 = 26;
  public static final int LLOAD_0 = 30;
  public static final int FLOAD_0 = 34;
  public static final int DLOAD_0 = 38;
  public static final int ALOAD_0 = 42;
  public static final int ISTORE_0 = 59;
  public static final int LSTORE_0 = 63;
  public static final int FSTORE_0 = 67;
  public static final int DSTORE_0 = 71;
  public static final int ASTORE_0 = 75;
  public static final int LDC2_W = 20;
}


/* Location:              /home/cfeindia/Desktop/kilim.jar!/kilim/Constants.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */