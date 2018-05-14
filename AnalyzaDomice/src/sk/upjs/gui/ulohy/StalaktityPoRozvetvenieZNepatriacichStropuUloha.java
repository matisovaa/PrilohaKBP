package sk.upjs.gui.ulohy;

import sk.upjs.service.AnalyzaServiceImplementacia;

public class StalaktityPoRozvetvenieZNepatriacichStropuUloha extends VypocetUloha {
	private AnalyzaServiceImplementacia service;
	private String cielovyAdresar;
	private String nazovSuboru;
	private boolean zohladnovatNormaly;
	private boolean doJednehoSuboru;
	private boolean ajOsamoteneBody;
	private float uholMaxOdchylky;
	private  int idxVrcholuZoStropu;

	public StalaktityPoRozvetvenieZNepatriacichStropuUloha(AnalyzaServiceImplementacia service, String cielovyAdresar,
			String nazovSuboru, boolean zohladnovatNormaly, boolean doJednehoSuboru, boolean ajOsamoteneBody,
			float uholMaxOdchylky, int idxVrcholuZoStropu) {

		this.service = service;
		this.cielovyAdresar = cielovyAdresar;
		this.nazovSuboru = nazovSuboru;
		this.zohladnovatNormaly = zohladnovatNormaly;
		this.doJednehoSuboru = doJednehoSuboru;
		this.ajOsamoteneBody = ajOsamoteneBody;
		this.uholMaxOdchylky = uholMaxOdchylky;
		this.idxVrcholuZoStropu = idxVrcholuZoStropu;
	}

	@Override
	public Boolean call() throws Exception {
		boolean vysledokOk = service.vsetkyStalaktityPoPrveRozvetvenieDoSuboru(cielovyAdresar, nazovSuboru,
				zohladnovatNormaly, doJednehoSuboru, ajOsamoteneBody, uholMaxOdchylky, idxVrcholuZoStropu);
		return vysledokOk;
	}

}
