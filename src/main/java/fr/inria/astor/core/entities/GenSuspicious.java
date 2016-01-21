package fr.inria.astor.core.entities;

import java.util.List;

import fr.inria.astor.core.faultlocalization.entity.SuspiciousCode;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtVariable;
/**
 * Gen created from a Suspicious code. 
 * That means, the gen is a suspicious to have a bug.
 * @author Matias Martinez,  matias.martinez@inria.fr
 *
 */
public class GenSuspicious extends Gen{

	protected SuspiciousCode suspicious;

	
	public GenSuspicious(){
		super();
	}
	
	public GenSuspicious(SuspiciousCode suspicious, CtElement rootElement, CtClass clonedClass,List<CtVariable> context) {
		super(rootElement, clonedClass, context);
		this.suspicious = suspicious;
	}
	public SuspiciousCode getSuspicious() {
		return suspicious;
	}
	public void setSuspicious(SuspiciousCode suspicious) {
		this.suspicious = suspicious;
	}
	
	public String toString(){
		return "Gen="
				+ ctClass.getSimpleName() +" line: "+suspicious.getLineNumber()+", el: "+codeElement.getClass().getSimpleName()+"";
	}
	
}
