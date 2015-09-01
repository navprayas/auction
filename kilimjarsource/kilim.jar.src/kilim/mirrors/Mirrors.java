/*    */ package kilim.mirrors;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ 
/*    */ public abstract class Mirrors
/*    */ {
/*    */   public abstract ClassMirror classForName(String paramString) throws ClassMirrorNotFoundException;
/*    */   
/*    */   public abstract ClassMirror mirror(Class<?> paramClass);
/*    */   
/*    */   public abstract MethodMirror mirror(Method paramMethod);
/*    */   
/*    */   public abstract MemberMirror mirror(java.lang.reflect.Member paramMember);
/*    */   
/*    */   public abstract FieldMirror mirror(java.lang.reflect.Field paramField);
/*    */   
/*    */   public static Mirrors getRuntimeMirrors()
/*    */   {
/* 19 */     return new RuntimeClassMirrors();
/*    */   }
/*    */ }


/* Location:              /home/cfeindia/Desktop/kilim.jar!/kilim/mirrors/Mirrors.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */