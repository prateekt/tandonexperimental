package api.matlab;
import util.sys.SysUtil;
import matlabcontrol.*;

public class MatlabControlUtil {
	
	private static RemoteMatlabProxy matlab;
	
	public static void main(String[] args) {
		try {
			
			getMatlab().eval("code");
			
//			getMatlab().setVariable("[wave,fs]", "wavread(a1.wav)");
			
//			rtn.append("[wave,fs]=wavread('" +audioFile+"');\n");

	//		System.out.println(getMatlab().returningEval("a=[5 5];", 1));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void runCode(String code, String file) {
		SysUtil.writeFile(file, code);
		try {
			Thread.sleep(1000);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		runFile(file.substring(0,file.indexOf(".")));
	}
	
	public static void runFile(String file) {
		try {
			getMatlab().eval("rehash");
			getMatlab().returningEval(file,0);
		} 
		catch (MatlabInvocationException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @return the matlab
	 */
	public static RemoteMatlabProxy getMatlab() {
		if(matlab==null) {
			try {
				RemoteMatlabProxyFactory fact = new RemoteMatlabProxyFactory();
				matlab = fact.getProxy();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		return matlab;
	}

	/**
	 * @param matlab the matlab to set
	 */
	public static void setMatlab(RemoteMatlabProxy matlab) {
		MatlabControlUtil.matlab = matlab;
	}	
}
