package sk.upjs.gui.ulohy;

import sk.upjs.service.AnalyzaServiceImplementacia;

public class StalaktityPoOddeleniStropuUloha extends VypocetUloha {
	private AnalyzaServiceImplementacia service;
	private String cielovyAdresar;
	private String nazovSuboru;
	private float uholMaxOdchylky;
	private int idxVrcholuZoStropu;
	private boolean ajSuborSOddelenymStropom;
	private boolean doJednehoSuboru;
	private boolean ajOsamoteneBody;

	public StalaktityPoOddeleniStropuUloha(AnalyzaServiceImplementacia service, String cielovyAdresar,
			String nazovSuboru, float uholMaxOdchylky, int idxVrcholuZoStropu, boolean ajSuborSOddelenymStropom,
			boolean doJednehoSuboru, boolean ajOsamoteneBody) {

		this.service = service;
		this.cielovyAdresar = cielovyAdresar;
		this.nazovSuboru = nazovSuboru;
		this.uholMaxOdchylky = uholMaxOdchylky;
		this.idxVrcholuZoStropu = idxVrcholuZoStropu;
		this.ajSuborSOddelenymStropom = ajSuborSOddelenymStropom;
		this.doJednehoSuboru = doJednehoSuboru;
		this.ajOsamoteneBody = ajOsamoteneBody;
	}

	@Override
	public Boolean call() throws Exception {
		boolean vysledokOk = service.vyrobSuborSoStalaktitmiPoOddeleniStropu(cielovyAdresar, nazovSuboru, uholMaxOdchylky,
				idxVrcholuZoStropu, ajSuborSOddelenymStropom, doJednehoSuboru, ajOsamoteneBody);
		return vysledokOk;
	}
}
