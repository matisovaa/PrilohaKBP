package sk.upjs.gui.ulohy;

import sk.upjs.service.AnalyzaServiceImplementacia;

public class KomponentySuvislostiUloha extends VypocetUloha {
		private AnalyzaServiceImplementacia service;
		private String cielovyAdresar;
		private String nazovSuboru;
		private boolean ajOsamoteneBody;

		public KomponentySuvislostiUloha(AnalyzaServiceImplementacia service, String cielovyAdresar, String nazovSuboru,
				boolean ajOsamoteneBody) {

			this.service = service;
			this.cielovyAdresar = cielovyAdresar;
			this.nazovSuboru = nazovSuboru;
			this.ajOsamoteneBody = ajOsamoteneBody;
		}

		@Override
		public Boolean call() throws Exception {
			boolean vysledokOk = service.vyrobSuborySKomponentmiSuvislosti(cielovyAdresar, nazovSuboru,
					ajOsamoteneBody);
			return vysledokOk;
		}
	}