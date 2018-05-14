package sk.upjs.gui.ulohy;

import sk.upjs.service.AnalyzaServiceImplementacia;

public class InicializaciaUloha extends VypocetUloha {
	private AnalyzaServiceImplementacia service;

	public InicializaciaUloha(AnalyzaServiceImplementacia service) {

		this.service = service;
	}

	@Override
	public Boolean call() throws Exception {
		boolean nacitanieOk = service.inicializacia();
		return nacitanieOk;
	}
}