package org.cloudio.morpheus;

import org.cloudio.morpheus.samples.*;
import org.objectweb.asm.*;
import org.objectweb.asm.util.TraceClassVisitor;
import scala.Function1;
import scala.runtime.AbstractFunction1;

import java.io.PrintWriter;

/**
* ASM Guide example class.
*
* @author Eric Bruneton
*/

/**
* ASM Guide example class.
*
* @author Eric Bruneton
*/
public class ClassPrinter implements ClassVisitor {
//
//    private static final FragmentB2Config fragmentB2Config = new FragmentB2Config() {
//        @Override
//        public MongoPersistence mm() {
//            return new MongoPersistenceImpl();
//        }
//
//        @Override
//        public String xx2() {
//            return "abc";
//        }
//    };
//
//    private static final AbstractFunction1<String, String> someFunction = new AbstractFunction1<String, String>() {
//        @Override
//        public String apply(String v1) {
//            return v1;
//        }
//    };
//
    public void visit(int version, int access, String name,
                      String signature, String superName, String[] interfaces) {
        System.out.println(name + " extends " + superName + " {");
    }

    public void visitSource(String source, String debug) {
    }

    public void visitOuterClass(String owner, String name, String desc) {
    }

    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        return null;
    }

    public void visitAttribute(Attribute attr) {
    }

    public void visitInnerClass(String name, String outerName,
                                String innerName, int access) {
    }

    public FieldVisitor visitField(int access, String name, String desc,
                                   String signature, Object value) {
        System.out.println("    " + desc + " " + name);
        return null;
    }

    public MethodVisitor visitMethod(int access, String name,
                                     String desc, String signature, String[] exceptions) {
        System.out.println("    " + name + desc);
        return null;
    }

    public void visitEnd() {
        System.out.println("}");
    }
//
////    public static Class instrumentFragmentClass(Class fragmentClass, String entityClassName) throws IOException {
////        ClassNode cn = new ClassNode();
////        ClassReader cr = new ClassReader(fragmentClass.getName());
////        cr.accept(cn, 0);
////        // here transform cn as you want
////
////        Iterator methodIter = cn.methods.iterator();
////
////        //MethodNode constructorNode = null;
////        while (methodIter.hasNext()) {
////            MethodNode methodNode = (MethodNode) methodIter.next();
////            ListIterator instructionsIter = methodNode.instructions.iterator();
////            while (instructionsIter.hasNext()) {
////                AbstractInsnNode insnNode = (AbstractInsnNode) instructionsIter.next();
////                if (insnNode.getOpcode() == Opcodes.CHECKCAST) {
////                    TypeInsnNode tcInsn = (TypeInsnNode) insnNode;
////                    if (entityClassName.equals(tcInsn.desc)) {
//////                        // insert MorphUtils.entity(this)
////                        methodNode.instructions.insertBefore(tcInsn,
////                                new MethodInsnNode(Opcodes.INVOKESTATIC,
////                                        "org/cloudio/morpheus/MorphUtils",
////                                        "context",
////                                        "(Ljava/lang/Object;)Ljava/lang/Object;"));
//////                        // remove CHECKCAST entityClass
//////                        constructorNode.instructions.remove(tcInsn.getPrevious());
//////                        constructorNode.instructions.remove(tcInsn.getNext());
//////                        constructorNode.instructions.remove(tcInsn);
////                    }
////                }
////
////            }
////        }
////
////        String newClassName = fragmentClass.getName();
////
////        ClassWriter cw = new ClassWriter(0);
////        cn.accept(cw);
////        byte[] b = cw.toByteArray();
////
////        //ClassReader cr2 = new ClassReader(b);
////        //printClass(cr2);
////
////        MyClassLoader myClassLoader = new MyClassLoader(Thread.currentThread().getContextClassLoader());
////        return myClassLoader.defineClass(newClassName, b);
////    }
//
////    public static class MyClassLoader extends ClassLoader {
////        public MyClassLoader(ClassLoader classLoader) {
////            super(classLoader);
////        }
////
////        @Override
////        public Class<?> loadClass(String s) throws ClassNotFoundException {
////            return super.loadClass(s);
////        }
////
////        public Class defineClass(String name, byte[] b) {
////            return defineClass(name, b, 0, b.length);
////        }
////    }
////
//
//
//    private static DimA dummyDimA = new DimA() {
//        @Override
//        public String convertToJson() {
//            return "AAAA";
//        }
//
//        @Override
//        public void convertFromJson(String json) {
//
//        }
//
//
//    };
//
//    public static Object createFragmentProxy(Class fragClass, String postfix, Class[] dependencies, Class[] fragArgTypes,
//                                             Object[] fragArgs) throws Exception {
//        FragmentInitContext initContext = new FragmentInitContext(fragClass, dependencies);
//        return FragmentEnhancer.createFragmentProxy(initContext, Class.forName(fragClass.getName() + "$" + postfix), fragArgTypes, fragArgs);
//    }
//
//    public static Object createFragmentWrapperProxy(Class fragClass, String postfix, Class[] dependencies, Class[] fragArgTypes,
//                                             Object[] fragArgs) throws Exception {
//        FragmentInitContext initContext = new FragmentInitContext(fragClass, dependencies);
//        return FragmentEnhancer.createFragmentWrapperProxy(initContext, Class.forName(fragClass.getName() + "$" + postfix), fragArgTypes, fragArgs);
//    }
//
//    public static void main(String[] args) throws Exception {
//        // a simple fragment without any configuration
//
//        // This abstract context is needed only during the construction of the fragment. The constructor may access
//        // the entity of the composite, e.g.
//        Object fragA2Proxy = createFragmentProxy(FragmentA2.class, "fragment", new Class[]{Entity1.class}, new Class[]{}, new Object[]{});
//
//        Object fragB2Proxy = createFragmentProxy(FragmentB2.class, "fragment", new Class[]{Entity1.class, DimA.class}, new Class[]{FragmentB2Config.class}, new Object[]{fragmentB2Config});
//
//        CompleteMorphContext compositeContext = new CompleteMorphContext(
//                new Class[]{Entity1.class, DimA.class, FragmentB2.class},
//                new Object[]{new Entity1(1), fragA2Proxy, fragB2Proxy});
//        Object compositeProxy = compositeContext.proxy();
//
//        Hierarchy2$.MODULE$.testComposite("Case 1", (Entity1) compositeProxy);
//
//        // filters
//
//        Object wrapperA2Proxy = createFragmentProxy(DimAWrapper2.class, "wrapper", new Class[]{Entity1.class}, new Class[0], new Object[0]);
//
//        Object wrappedFragA2 = FilterChainProxy.apply(fragA2Proxy, FragmentA2.class, wrapperA2Proxy);
//
//        compositeContext = new CompleteMorphContext(
//                new Class[]{Entity1.class, DimA.class, FragmentB2.class},
//                        new Object[]{new Entity1(2), wrappedFragA2, fragB2Proxy});
//        compositeProxy = compositeContext.proxy();
//
//        Hierarchy2$.MODULE$.testComposite("Case 2", (Entity1) compositeProxy);
//
//        Object wrapperB2Proxy = createFragmentWrapperProxy(FragmentB2Wrapper.class, "wrapper", new Class[]{Entity1.class, DimA.class}, new Class[0], new Object[0]);
//
//        Object wrappedFragB2 = FilterChainProxy.apply(fragB2Proxy, FragmentB2Wrapper.class, wrapperB2Proxy);
//
//        compositeContext = new CompleteMorphContext(
//                new Class[]{Entity1.class, DimA.class, FragmentB2Wrapper.class},
//                new Object[]{new Entity1(2), wrappedFragA2, wrappedFragB2});
//        compositeProxy = compositeContext.proxy();
//
//        Hierarchy2$.MODULE$.testComposite("Case 3", (Entity1) compositeProxy);
//
//
//        FragmentB2Wrapper2Config f2cfg = new FragmentB2Wrapper2Config() {
//            @Override
//            public String zumo() {
//                return "ZUMO";
//            }
//        };
//
//        Object wrapperB22Proxy = createFragmentWrapperProxy(
//                FragmentB2Wrapper2.class, "wrapper", new Class[]{Entity1.class, DimA.class}, new Class[]{FragmentB2Wrapper2Config.class}, new Object[]{f2cfg});
//
//        Object wrappedFragB22 = FilterChainProxy.apply(fragB2Proxy, FragmentB2Wrapper2.class, wrapperB22Proxy);
//
//        compositeContext = new CompleteMorphContext(
//                new Class[]{Entity1.class, DimA.class, FragmentB2Wrapper2.class},
//                new Object[]{new Entity1(2), wrappedFragA2, wrappedFragB22});
//        compositeProxy = compositeContext.proxy();
//
//        Hierarchy2$.MODULE$.testComposite("Case 4", (Entity1) compositeProxy);
//
//////
//////        MorphContextCGLIB wrapperA3Context =
//////                new MorphContextCGLIB(new Class[]{DimA.class}, new Class[]{Object.class}, null);
//////        Object wrapperA3Proxy = createFragmentProxy(
//////                wrapperA3Context,
//////                DimAWrapper2Wrapper_fragment.class,
//////                new Class[0],
//////                new Object[0]);
//////
//////        Object wrappedFragA3 = FilterChainProxy.apply(wrappedFragA2, FragmentA2.class, wrapperA3Proxy);
//////
//////        MorphContextCGLIB wrapperA4Context =
//////                new MorphContextCGLIB(new Class[]{DimA.class}, new Class[]{Object.class}, null);
//////        Object wrapperA4Proxy = createFragmentProxy(
//////                wrapperA4Context,
//////                DimAWrapper3Wrapper_fragment.class,
//////                new Class[0],
//////                new Object[0]);
//////
//////        Object wrappedFragA4 = FilterChainProxy.apply(wrappedFragA3, FragmentA2.class, wrapperA4Proxy);
////
////        compositeContext = new MorphContextCGLIB(new Class[0],
////                new Class[]{Entity1.class, FragmentA2.class, FragmentB2Wrapper.class},
////                new Object[]{new Entity1(2), wrappedFragA2, wrappedFragB2});
////        compositeProxy = compositeContext.proxy();
////
////        DimA dimA = (DimA) compositeContext.proxy();
////        dimA.convertFromJson("A");
////        String s = dimA.convertToJson();
////        System.out.println(s);
////
////        DimB dimB = (DimB) compositeContext.proxy();
////        dimB.load();
////        dimB.store();
////
////        //Hierarchy2$.MODULE$.testComposite((Entity1) compositeProxy);
////
////        // indirect morphing is initiated by an event
//////        MorphContext morphedComposite = compositeContext.morph("RED");
////
//////        System.out.println(compositeProxy.toString());
//////
//////        Entity1 e1 = (Entity1) compositeProxy;
//////        System.out.println(e1.id());
//////
//////        DimA dimA = (DimA) compositeProxy;
//////        System.out.println(dimA.convertToJson());
//////
//////        DimB dimB = (DimB) compositeProxy;
//////        dimB.load();
////
//    }
//
    public static void main(String[] args) throws Exception {
        //ClassPrinter cp = new ClassPrinter();
        //ClassReader cr = new ClassReader("org.iqual.dyn.user.EntityA");
        //ClassReader cr = new ClassReader("org.cloudio.morpheus.samples.DerivedClass");
        //ClassReader cr = new ClassReader("org.cloudio.morpheus.samples.Trait3$class");
        //ClassReader cr = new ClassReader("org.cloudio.morpheus.samples.Trait2");
        //ClassReader cr = new ClassReader("org.cloudio.morpheus.samples.Trait3");
        //ClassReader cr = new ClassReader("org.cloudio.morpheus.samples.Trait3fragment");
        //ClassReader cr = new ClassReader("org.cloudio.morpheus.samples.Sample1");
        //ClassReader cr = new ClassReader("org.cloudio.morpheus.samples.FragmentB2$class");
        //ClassReader cr = new ClassReader("org.cloudio.morpheus.samples.FragmentB2Base$class");
        //ClassReader cr = new ClassReader("org.cloudio.morpheus.samples.FragmentB2$fragment");
        //ClassReader cr = new ClassReader("org.cloudio.morpheus.samples.DimAWrapper1$class");
        //ClassReader cr = new ClassReader("org.cloudio.morpheus.samples.DimAWrapper1");
        //ClassReader cr = new ClassReader("org.cloudio.morpheus.samples.DimAWrapper11$class");
        //ClassReader cr = new ClassReader("org.cloudio.morpheus.samples.DimAWrapper1Wrapper_fragment");
        //ClassReader cr = new ClassReader("org.cloudio.morpheus.samples.DimAWrapper11Wrapper_fragment");
        //ClassReader cr = new ClassReader("org/cloudio/morpheus/samples/DimAWrapper1_base");
        //ClassReader cr = new ClassReader("org/cloudio/morpheus/samples/FragmentB2Wrapper");
        //ClassReader cr = new ClassReader("org/cloudio/morpheus/samples/FragmentB2Wrapper$wrapper");
        //ClassReader cr = new ClassReader("org/cloudio/morpheus/samples/FragmentB2Wrapper$class");
        //ClassReader cr = new ClassReader("org/cloudio/morpheus/samples/FragmentB2Wrapper2$fragment");
        ClassReader cr = new ClassReader("org/cloudio/morpheus/EntityALogger$fragment");
        //ClassReader cr = new ClassReader("org/cloudio/morpheus/samples/FragmentB2Wrapper2$class");
        //ClassReader cr = new ClassReader("org/cloudio/morpheus/samples/DimAWrapper5$wrapper");
        //ClassReader cr = new ClassReader("org/cloudio/morpheus/samples/FragmentB2$fragment");
        printClass(cr);
    }

    static void printClass(ClassReader cr) {
        ClassWriter cw = new ClassWriter(0);
        PrintWriter printWriter = new PrintWriter(System.out);
        TraceClassVisitor cv = new TraceClassVisitor(cw, printWriter);
        cr.accept(cv, 0);
    }

    public static void main3(String[] args) throws Exception {
//        DimensionWrapperProxy<DimAWrapper1> wFact =
//                new DimensionWrapperProxy<DimAWrapper1>(DimAWrapper1.class, DimAWrapper1$class.class);
//
//        DimAWrapper1 w = wFact.apply();
//        w.convertFromJson("abc");
    }
}