package sk.upjs.gui.objemUlohy;

import sk.upjs.service.AnalyzaServiceImplementacia;

public class ObjemLenStalaktitovUloha extends ObjemUloha {
	
	private AnalyzaServiceImplementacia service;	
	private boolean zohladnovatNormaly;
	private float vyskaUseku;

	public ObjemLenStalaktitovUloha(AnalyzaServiceImplementacia service, boolean zohladnovatNormaly, float vyskaUseku) {
		this.service = service;		
		this.zohladnovatNormaly = zohladnovatNormaly;
		this.vyskaUseku = vyskaUseku;
	}

	@Override
	public Double call() throws Exception {
		double objem = service.objemStalaktitovZoSuboruLenSoStalaktitmi(vyskaUseku, zohladnovatNormaly);
		return objem;
	}

}
