/*     */ package kilim.mirrors;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import org.objectweb.asm.Type;
/*     */ 
/*     */ class RuntimeClassMirrors extends Mirrors
/*     */ {
/*     */   static class RuntimeMemberMirror implements MemberMirror
/*     */   {
/*     */     private final Member member;
/*     */     
/*     */     public RuntimeMemberMirror(Member member)
/*     */     {
/*  16 */       this.member = member;
/*     */     }
/*     */     
/*     */     public String getName() {
/*  20 */       return this.member.getName();
/*     */     }
/*     */   }
/*     */   
/*     */   static class RuntimeFieldMirror extends RuntimeClassMirrors.RuntimeMemberMirror implements FieldMirror
/*     */   {
/*     */     private final Field field;
/*     */     
/*     */     public RuntimeFieldMirror(Field field)
/*     */     {
/*  30 */       super();
/*  31 */       this.field = field;
/*     */     }
/*     */     
/*     */     public static FieldMirror forField(Field member) {
/*  35 */       if (member == null) return null;
/*  36 */       return new RuntimeFieldMirror(member);
/*     */     }
/*     */     
/*     */     public ClassMirror getType() {
/*  40 */       return RuntimeClassMirrors.RuntimeClassMirror.forClass(this.field.getType());
/*     */     }
/*     */   }
/*     */   
/*     */   static class RuntimeMethodMirror extends RuntimeClassMirrors.RuntimeMemberMirror implements MethodMirror
/*     */   {
/*     */     private final Method method;
/*     */     
/*     */     public RuntimeMethodMirror(Method method) {
/*  49 */       super();
/*  50 */       this.method = method;
/*     */     }
/*     */     
/*     */     static MethodMirror forMethod(Method method) {
/*  54 */       if (method == null) return null;
/*  55 */       return new RuntimeMethodMirror(method);
/*     */     }
/*     */     
/*     */     public static MethodMirror[] forMethods(Method[] declaredMethods) {
/*  59 */       MethodMirror[] result = new MethodMirror[declaredMethods.length];
/*  60 */       for (int i = 0; i < declaredMethods.length; i++) {
/*  61 */         result[i] = forMethod(declaredMethods[i]);
/*     */       }
/*  63 */       return result;
/*     */     }
/*     */     
/*     */     public ClassMirror[] getExceptionTypes() {
/*  67 */       return RuntimeClassMirrors.RuntimeClassMirror.forClasses(this.method.getExceptionTypes());
/*     */     }
/*     */     
/*     */     public String getMethodDescriptor() {
/*  71 */       return Type.getMethodDescriptor(this.method);
/*     */     }
/*     */     
/*     */     public boolean isBridge() {
/*  75 */       return this.method.isBridge();
/*     */     }
/*     */   }
/*     */   
/*     */   static class RuntimeClassMirror extends ClassMirror
/*     */   {
/*     */     private final Class<?> clazz;
/*     */     
/*     */     public RuntimeClassMirror(Class<?> clazz)
/*     */     {
/*  85 */       if (clazz == null) throw new NullPointerException();
/*  86 */       this.clazz = clazz;
/*     */     }
/*     */     
/*     */     public String getName()
/*     */     {
/*  91 */       return this.clazz.getName();
/*     */     }
/*     */     
/*     */     public boolean isInterface()
/*     */     {
/*  96 */       return this.clazz.isInterface();
/*     */     }
/*     */     
/*     */     public boolean equals(Object obj)
/*     */     {
/* 101 */       if ((obj instanceof RuntimeClassMirror)) {
/* 102 */         RuntimeClassMirror mirr = (RuntimeClassMirror)obj;
/*     */         
/* 104 */         return mirr.clazz == this.clazz;
/*     */       }
/*     */       
/* 107 */       return false;
/*     */     }
/*     */     
/*     */     public MethodMirror[] getDeclaredMethods()
/*     */     {
/* 112 */       return RuntimeClassMirrors.RuntimeMethodMirror.forMethods(this.clazz.getDeclaredMethods());
/*     */     }
/*     */     
/*     */     public ClassMirror[] getInterfaces()
/*     */     {
/* 117 */       return forClasses(this.clazz.getInterfaces());
/*     */     }
/*     */     
/*     */     private static ClassMirror forClass(Class<?> clazz) {
/* 121 */       if (clazz == null) return null;
/* 122 */       return new RuntimeClassMirror(clazz);
/*     */     }
/*     */     
/*     */     private static ClassMirror[] forClasses(Class<?>[] classes) {
/* 126 */       ClassMirror[] result = new ClassMirror[classes.length];
/* 127 */       for (int i = 0; i < classes.length; i++) {
/* 128 */         result[i] = forClass(classes[i]);
/*     */       }
/* 130 */       return result;
/*     */     }
/*     */     
/*     */     public ClassMirror getSuperclass()
/*     */     {
/* 135 */       return forClass(this.clazz.getSuperclass());
/*     */     }
/*     */     
/*     */     public boolean isAssignableFrom(ClassMirror c)
/*     */     {
/* 140 */       if ((c instanceof RuntimeClassMirror)) {
/* 141 */         RuntimeClassMirror cc = (RuntimeClassMirror)c;
/* 142 */         return this.clazz.isAssignableFrom(cc.clazz);
/*     */       }
/* 144 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public ClassMirror classForName(String className)
/*     */     throws ClassMirrorNotFoundException
/*     */   {
/*     */     try
/*     */     {
/* 154 */       return new RuntimeClassMirror(Class.forName(className));
/*     */     } catch (ClassNotFoundException e) {
/* 156 */       throw new ClassMirrorNotFoundException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public ClassMirror mirror(Class<?> clazz)
/*     */   {
/* 162 */     if (clazz == null) return null;
/* 163 */     return new RuntimeClassMirror(clazz);
/*     */   }
/*     */   
/*     */   public MethodMirror mirror(Method mth)
/*     */   {
/* 168 */     if (mth == null) return null;
/* 169 */     return RuntimeMethodMirror.forMethod(mth);
/*     */   }
/*     */   
/*     */   public FieldMirror mirror(Field member)
/*     */   {
/* 174 */     return RuntimeFieldMirror.forField(member);
/*     */   }
/*     */   
/*     */   public MemberMirror mirror(Member member)
/*     */   {
/* 179 */     if ((member instanceof Method))
/* 180 */       return mirror((Method)member);
/* 181 */     if ((member instanceof Field)) {
/* 182 */       return mirror((Field)member);
/*     */     }
/* 184 */     throw new RuntimeException("member is not field or method?");
/*     */   }
/*     */ }


/* Location:              /home/cfeindia/Desktop/kilim.jar!/kilim/mirrors/RuntimeClassMirrors.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */