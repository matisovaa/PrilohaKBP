package sk.upjs.gui.ulohy;

import sk.upjs.service.AnalyzaServiceImplementacia;

public class LokalneMinimaNepatriaceStropuUloha extends VypocetUloha {
	private AnalyzaServiceImplementacia service;
	private String cielovyAdresar;
	private String nazovSuboru;
	private float uholMaxOdchylky;
	private int idxVrcholuZoStropu;
	private boolean zohladnovatNormaly;

	public LokalneMinimaNepatriaceStropuUloha(AnalyzaServiceImplementacia service, String cielovyAdresar,
			String nazovSuboru, float uholMaxOdchylky, int idxVrcholuZoStropu, boolean zohladnovatNormaly) {

		this.service = service;
		this.cielovyAdresar = cielovyAdresar;
		this.nazovSuboru = nazovSuboru;
		this.uholMaxOdchylky = uholMaxOdchylky;
		this.idxVrcholuZoStropu = idxVrcholuZoStropu;
		this.zohladnovatNormaly = zohladnovatNormaly;

	}

	@Override
	public Boolean call() throws Exception {
		boolean vysledokOk = service.vyrobSuborSMinimamiKtoreNepatriaStropu(cielovyAdresar, nazovSuboru,
				uholMaxOdchylky, idxVrcholuZoStropu, zohladnovatNormaly);
		return vysledokOk;
	}

}
