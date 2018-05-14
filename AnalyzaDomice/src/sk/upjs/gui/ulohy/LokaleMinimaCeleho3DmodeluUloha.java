package sk.upjs.gui.ulohy;

import sk.upjs.service.AnalyzaServiceImplementacia;

public class LokaleMinimaCeleho3DmodeluUloha extends VypocetUloha {

		private AnalyzaServiceImplementacia service;
		private String cielovyAdresar;
		private String nazovSuboru;
		private boolean zohladnovatNormaly;

		public LokaleMinimaCeleho3DmodeluUloha(AnalyzaServiceImplementacia service, String cielovyAdresar,
				String nazovSuboru, boolean zohladnovatNormaly) {

			this.service = service;
			this.cielovyAdresar = cielovyAdresar;
			this.nazovSuboru = nazovSuboru;
			this.zohladnovatNormaly = zohladnovatNormaly;
		}

		@Override
		public Boolean call() throws Exception {
			boolean vysledokOk = service.vyrobSuborSLokalnymiMinimamiCeleho3Dmodelu(cielovyAdresar, nazovSuboru,
					zohladnovatNormaly);
			return vysledokOk;
		}

	}