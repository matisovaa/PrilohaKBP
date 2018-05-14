package sk.upjs.gui.ulohy;

import sk.upjs.service.AnalyzaServiceImplementacia;

public class StalaktityPoRozvetvenieZoVsetkychUloha extends VypocetUloha {
		private AnalyzaServiceImplementacia service;
		private String cielovyAdresar;
		private String nazovSuboru;
		private boolean zohladnovatNormaly;
		private boolean doJednehoSuboru;
		private boolean ajOsamoteneBody;

		public StalaktityPoRozvetvenieZoVsetkychUloha(AnalyzaServiceImplementacia service, String cielovyAdresar,
				String nazovSuboru, boolean zohladnovatNormaly, boolean doJednehoSuboru, boolean ajOsamoteneBody) {

			this.service = service;
			this.cielovyAdresar = cielovyAdresar;
			this.nazovSuboru = nazovSuboru;
			this.zohladnovatNormaly = zohladnovatNormaly;
			this.doJednehoSuboru = doJednehoSuboru;
			this.ajOsamoteneBody = ajOsamoteneBody;
		}

		@Override
		public Boolean call() throws Exception {
			boolean vysledokOk = service.vsetkyStalaktityPoPrveRozvetvenieDoSuboru(cielovyAdresar, nazovSuboru,
					zohladnovatNormaly, doJednehoSuboru, ajOsamoteneBody);
			return vysledokOk;
		}
	}