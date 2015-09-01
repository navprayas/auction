package kilim.mirrors;

public abstract interface MethodMirror
  extends MemberMirror
{
  public abstract String getMethodDescriptor();
  
  public abstract ClassMirror[] getExceptionTypes();
  
  public abstract boolean isBridge();
}


/* Location:              /home/cfeindia/Desktop/kilim.jar!/kilim/mirrors/MethodMirror.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */