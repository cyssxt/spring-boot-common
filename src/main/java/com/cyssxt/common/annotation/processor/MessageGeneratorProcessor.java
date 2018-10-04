package com.cyssxt.common.annotation.processor;


import com.cyssxt.common.annotation.Message;
import com.cyssxt.common.message.MessageGenerator;
import com.cyssxt.common.message.MessageHelper;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
@SupportedAnnotationTypes({"com.cyssxt.common.annotation.Message"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class MessageGeneratorProcessor extends AbstractProcessor {

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        Messager messager = processingEnv.getMessager();
        Elements elementsUtils = processingEnv.getElementUtils();

        // 在这里打印gradle文件传进来的参数
        Map<String, String> map = processingEnv.getOptions();
        for (String key : map.keySet()) {
            System.out.println(key+"===========");
            messager.printMessage(Diagnostic.Kind.NOTE, "key" + "：" + map.get(key));
        }

    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement typeElement : annotations) {
            for (Element element : roundEnv
                    .getElementsAnnotatedWith(typeElement)) {
                //获取Annotation
                Message message = element
                        .getAnnotation(Message.class);
                if (message != null) {
                    String basePackage = message.value();
                    basePackage = basePackage.replace(".", File.separator);
                    try {
                        MessageHelper.init(basePackage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
        return false;
    }
}
