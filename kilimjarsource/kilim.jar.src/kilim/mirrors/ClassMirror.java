package kilim.mirrors;

public abstract class ClassMirror
{
  public abstract MethodMirror[] getDeclaredMethods();
  
  public abstract boolean isAssignableFrom(ClassMirror paramClassMirror);
  
  public abstract ClassMirror getSuperclass();
  
  public abstract ClassMirror[] getInterfaces();
  
  public abstract boolean isInterface();
  
  public abstract String getName();
}


/* Location:              /home/cfeindia/Desktop/kilim.jar!/kilim/mirrors/ClassMirror.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */