package sk.upjs.gui.objemUlohy;

import sk.upjs.service.AnalyzaServiceImplementacia;

public class ObjemVyseparovanychUloha extends ObjemUloha {

		private AnalyzaServiceImplementacia service;
		private float uholMaxOdchylky;
		private int idxVrcholuZoStropu;
		private boolean zohladnovatNormaly;
		private float vyskaUseku;

		public ObjemVyseparovanychUloha(AnalyzaServiceImplementacia service, float uholMaxOdchylky,
				int idxVrcholuZoStropu, boolean zohladnovatNormaly, float vyskaUseku) {
			this.service = service;
			this.uholMaxOdchylky = uholMaxOdchylky;
			this.idxVrcholuZoStropu = idxVrcholuZoStropu;
			this.zohladnovatNormaly = zohladnovatNormaly;
			this.vyskaUseku = vyskaUseku;
		}

		@Override
		public Double call() throws Exception {
			double objem = service.objemVyseparovanychStalaktitovZCeleho3DModelu(uholMaxOdchylky, idxVrcholuZoStropu,
					vyskaUseku, zohladnovatNormaly);
			return objem;
		}

	}