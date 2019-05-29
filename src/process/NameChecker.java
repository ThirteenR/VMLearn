package process;

import com.sun.org.apache.xerces.internal.dom.ElementNSImpl;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.util.ElementScanner6;

import java.util.EnumSet;

import static javax.lang.model.element.ElementKind.*;
import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;
import static javax.lang.model.element.Modifier.STATIC;
import static javax.tools.Diagnostic.Kind.WARNING;

/**
 * Author: rsq0113
 * Date: 2019-05-29 16:54
 * Description:
 **/
public class NameChecker {
    private final Messager messager;
    NameCheckerScanner nameCheckerScanner = new NameCheckerScanner();
    public NameChecker(ProcessingEnvironment processingEnv) {
        this.messager = processingEnv.getMessager();
    }
    
    public void checkNames(Element element){
        nameCheckerScanner.scan(element);
    }


    private class NameCheckerScanner extends ElementScanner6<Void,Void> {
        /*���Java��*/
        @Override
        public Void visitType(TypeElement e, Void aVoid) {
            return super.visitType(e, aVoid);
        }
        /*��鷽�����Ƿ�Ϸ�*/
        @Override
        public Void visitExecutable(ExecutableElement e, Void aVoid) {
            if(e.getKind() == METHOD){
                Name name = e.getSimpleName();
            if(name.contentEquals(e.getEnclosingElement().getSimpleName())){
                messager.printMessage(WARNING,"��ͨ������\""+name+"\"��Ӧ���������ظ�",e);
                checkCamelCase(e,false);
            }
            }
            super.visitExecutable(e, aVoid);
            return null;
        }
        /*���������Ƿ�Ϸ�*/

        @Override
        public Void visitVariable(VariableElement e, Void aVoid) {
            if(e.getKind() == ENUM_CONSTANT || e.getConstantValue() != null || heuristicallyConstant(e)){
                checkAllCaps(e);
            }else{
                checkCamelCase(e,false);
            }
            return null;
        }
        /*�ж�һ�������Ƿ��ǳ���*/
        private boolean heuristicallyConstant(VariableElement e){
            if (e.getEnclosingElement().getKind() == INTERFACE){
                return true;
            }else if(e.getKind() == FIELD && e.getModifiers().containsAll(EnumSet.of(PUBLIC,STATIC,FINAL))){
                return true;
            }else {
                return false;
            }
        }
        /*��鴫���Element�Ƿ�����շ�����������������ϣ������������Ϣ*/
        private void checkCamelCase(Element e,boolean initialCaps){
            String  name = e.getSimpleName().toString();
            boolean previousUpper = false;
            boolean conventional = true;
            int firstCodePoint = name.codePointAt(0);
            if(Character.isUpperCase(firstCodePoint)){
                previousUpper = true;
                if(!initialCaps){
                    messager.printMessage(WARNING,"����\""+name+"\"Ӧ����Сд��ĸ��ͷ",e);
                    return;
                }
            }else if(Character.isLowerCase(firstCodePoint)){
                if(initialCaps){
                    messager.printMessage(WARNING,"����\""+name+"\"Ӧ���Դ�д��ĸ��ͷ",e);
                    return;
                }
            }else {
                conventional = false;
            }
            if(conventional){
                int cp = firstCodePoint;
                for(int i = Character.charCount(cp);i<name.length();i+=Character.charCount(cp)){
                    cp = name.codePointAt(i);
                    if(Character.isUpperCase(cp)){
                        if (previousUpper){
                            conventional = false;
                            break;
                        }
                        previousUpper = true;
                    }else{
                        previousUpper = false;
                    }
                }
            }
            if(!conventional){
                messager.printMessage(WARNING,"����\""+name+"\"Ӧ�������շ�������",e);
            }
        }
        /*��д������飬Ҫ���һ����ĸ�����Ǵ�д��Ӣ����ĸ�����ಿ�ֿ���ʹ�»��߻��ߴ�д��ĸ*/
        private void checkAllCaps(Element e){
            String name = e.getSimpleName().toString();
            boolean conventional = true;
            int firstCodePoint = name.codePointAt(0);
            if(!Character.isUpperCase(firstCodePoint)){
                conventional = false;
            }else{
                boolean previousUnderscore = false;

            int cp = firstCodePoint;
            for(int i = Character.charCount(cp);i<name.length();i+=Character.charCount(cp)){
                cp = name.codePointAt(i);
                if(cp==(int)'_'){
                    if(previousUnderscore){
                        conventional = false;
                        break;
                    }
                    previousUnderscore = true;
                }else{
                    previousUnderscore = false;
                    if(!Character.isUpperCase(cp) && !Character.isDigit(cp)){
                        conventional = false;
                        break;
                    }
                }
            }
            }
            if(!conventional){
                messager.printMessage(WARNING,"����\""+name+"\"Ӧ��ȫ��һ��Щ��ĸ���»�����������ĸ��ͷ",e);
            }
        }
        @Override
        public Void scan(Element e, Void aVoid) {
            return super.scan(e, aVoid);
        }
    }
}
