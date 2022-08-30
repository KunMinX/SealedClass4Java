package com.kunminx.sealed.apt;

import com.google.auto.service.AutoService;
import com.kunminx.sealed.annotation.Param;
import com.kunminx.sealed.annotation.SealedClass;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.util.Elements;

/**
 * Create by KunMinX at 2022/7/20
 */
@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class SealedClassProcessor extends AbstractProcessor {
  private Filer mFiler;
  private Elements mElementUtils;

  @Override
  public synchronized void init(ProcessingEnvironment pEnv) {
    super.init(pEnv);
    mElementUtils = pEnv.getElementUtils();
    mFiler = pEnv.getFiler();
  }

  @Override
  public boolean process(Set<? extends TypeElement> types, RoundEnvironment rEnv) {
    if (types == null || types.isEmpty()) {
      return false;
    }
    Set<? extends Element> rootElements = rEnv.getElementsAnnotatedWith(SealedClass.class);
    if (rootElements != null && !rootElements.isEmpty()) {
      for (Element element : rootElements) {
        String name = element.getSimpleName().toString();
        String className = name.replace("_", "");
        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(className).addModifiers(Modifier.PUBLIC);

        FieldSpec fbId = FieldSpec.builder(ClassName.get(String.class), "id")
                .addModifiers(Modifier.PUBLIC)
                .addModifiers(Modifier.FINAL)
                .build();
        classBuilder.addField(fbId);

        MethodSpec consId = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(String.class, "id")
                .addStatement("this.$N = $N", "id", "id")
                .build();
        classBuilder.addMethod(consId);

        TypeElement typeElement = (TypeElement) element;
        List<? extends Element> members = mElementUtils.getAllMembers(typeElement);
        for (Element childElement : members) {
          if (childElement instanceof ExecutableElement) {
            ExecutableElement executableElement = (ExecutableElement) childElement;
            String methodName = executableElement.getSimpleName().toString();
            boolean isContinue = false;
            switch (methodName) {
              case "getClass":
              case "hashCode":
              case "equals":
              case "toString":
              case "notify":
              case "notifyAll":
              case "wait":
                isContinue = true;
              default:
            }
            if (isContinue) continue;
            String innerClassName = upperCaseFirst(executableElement.getSimpleName().toString());

            TypeSpec.Builder innerClassBuilder = TypeSpec.classBuilder(innerClassName)
                    .addModifiers(Modifier.PUBLIC)
                    .addModifiers(Modifier.STATIC)
                    .superclass(ClassName.get(typeElement.getEnclosingElement().toString(), className));

            FieldSpec fbID = FieldSpec.builder(ClassName.get(String.class), "ID")
                    .addModifiers(Modifier.PUBLIC)
                    .addModifiers(Modifier.FINAL)
                    .addModifiers(Modifier.STATIC)
                    .initializer("$S", ClassName.get(typeElement.getEnclosingElement().toString(), className) + "." + innerClassName)
                    .build();
            innerClassBuilder.addField(fbID);

            List<? extends VariableElement> parameters = executableElement.getParameters();
            List<ParameterSpec> ps = new ArrayList<>();
            List<ParameterSpec> psParams = new ArrayList<>();
            List<ParameterSpec> psResults = new ArrayList<>();

            MethodSpec.Builder consInnerBuilder = MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC)
                    .addStatement("super($N.class.getName())", innerClassName);

            MethodSpec.Builder mtCopyBuilder = MethodSpec.methodBuilder("copy")
                    .addModifiers(Modifier.PUBLIC);

            MethodSpec.Builder mtInsBuilder = MethodSpec.methodBuilder(innerClassName)
                    .addModifiers(Modifier.PUBLIC)
                    .addModifiers(Modifier.STATIC)
                    .returns(ClassName.get(typeElement.getEnclosingElement().toString(), className));

            MethodSpec.Builder mtInsBuilder1 = MethodSpec.methodBuilder(innerClassName)
                    .addModifiers(Modifier.PUBLIC)
                    .addModifiers(Modifier.STATIC)
                    .returns(ClassName.get(typeElement.getEnclosingElement().toString(), className));

            StringBuilder sb = new StringBuilder();
            StringBuilder sbStatic = new StringBuilder();

            if (parameters.size() > 0) {
              for (VariableElement ve : parameters) {
                String paramName;
                boolean isParam = false;
                if (ve.getAnnotation(Param.class) != null) {
                  paramName = "param" + upperCaseFirst(ve.getSimpleName().toString());
                  isParam = true;
                } else {
                  paramName = "result" + upperCaseFirst(ve.getSimpleName().toString());
                }

                FieldSpec fbInner = FieldSpec.builder(TypeName.get(ve.asType()), paramName)
                        .addModifiers(Modifier.PUBLIC)
                        .addModifiers(Modifier.FINAL)
                        .build();
                innerClassBuilder.addField(fbInner);
                ps.add(ParameterSpec.get(ve));
                if (isParam) psParams.add(ParameterSpec.get(ve));
                if (!isParam) psResults.add(ParameterSpec.get(ve));
                consInnerBuilder.addStatement("this.$N = $N", paramName, ve.getSimpleName().toString());
                if (isParam) sb.append("this.").append(paramName).append(",");
                else sb.append(ve.getSimpleName().toString()).append(",");
                if (isParam) sbStatic.append(ve.getSimpleName().toString()).append(",");
                else sbStatic.append(getDefaultValue(ve)).append(",");
              }
              String sbb = sb.toString();
              sbb = sbb.substring(0, sbb.length() - 1);
              String sbbStatic = sbStatic.toString();
              sbbStatic = sbbStatic.substring(0, sbbStatic.length() - 1);
              consInnerBuilder.addParameters(ps);
              mtCopyBuilder.addParameters(psResults);
              mtCopyBuilder.returns(ClassName.get(typeElement.getEnclosingElement().toString(), className + "." + innerClassName));
              mtCopyBuilder.addStatement("return new $N($N)", innerClassName, sbb);
              innerClassBuilder.addMethod(mtCopyBuilder.build());
              mtInsBuilder.addParameters(psParams);
              mtInsBuilder.addStatement("return new $N($N)", innerClassName, sbbStatic);
              if (psParams.size() == 0) {
                mtInsBuilder1.addParameters(psResults);
                mtInsBuilder1.addStatement("return new $N($N)", innerClassName, sbb);
                classBuilder.addMethod(mtInsBuilder1.build());
              }
            } else {
              mtInsBuilder.addStatement("return new $N()", innerClassName);
            }
            innerClassBuilder.addMethod(consInnerBuilder.build());
            classBuilder.addType(innerClassBuilder.build());
            classBuilder.addMethod(mtInsBuilder.build());
          }
        }

        TypeSpec typeSpec = classBuilder.build();
        JavaFile javaFile = JavaFile.builder(typeElement.getEnclosingElement().toString(), typeSpec)
                .build();
        try {
          javaFile.writeTo(mFiler);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return true;
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    HashSet<String> hashSet = new HashSet<>();
    hashSet.add(SealedClass.class.getCanonicalName());
    hashSet.add(Param.class.getCanonicalName());
    return hashSet;
  }

  private String upperCaseFirst(String val) {
    char[] arr = val.toCharArray();
    arr[0] = Character.toUpperCase(arr[0]);
    return new String(arr);
  }

  private String getDefaultValue(VariableElement ve) {
    TypeKind kind = ve.asType().getKind();
    switch (kind) {
      case BOOLEAN:
        return "false";
      case INT:
        return "0";
      case LONG:
        return "0L";
      case FLOAT:
        return "0f";
      case DOUBLE:
        return "0.0";
      default:
        return "null";
    }
  }
}