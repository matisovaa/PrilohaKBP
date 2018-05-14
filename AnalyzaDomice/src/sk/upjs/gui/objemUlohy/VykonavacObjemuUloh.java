package sk.upjs.gui.objemUlohy;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import sk.upjs.gui.VykonavaSaUlohaAStav;
import sk.upjs.service.AnalyzaServiceImplementacia;

public class VykonavacObjemuUloh implements Runnable {

	private ObjemUloha uloha;
	private VykonavaSaUlohaAStav vykonavaSaUlohaAStav;
	
	public VykonavacObjemuUloh(VykonavaSaUlohaAStav vykonavaSaUloha, AnalyzaServiceImplementacia service, float uholMaxOdchylky,
			int idxVrcholuZoStropu, boolean zohladnovatNormaly, float vyskaUseku) {
		uloha = new ObjemVyseparovanychUloha(service, uholMaxOdchylky, idxVrcholuZoStropu, zohladnovatNormaly, vyskaUseku);
		this.vykonavaSaUlohaAStav = vykonavaSaUloha;
	}

	public VykonavacObjemuUloh(VykonavaSaUlohaAStav vykonavaSaUloha, AnalyzaServiceImplementacia service,
			boolean zohladnovatNormaly, float vyskaUseku) {
		uloha = new ObjemLenStalaktitovUloha(service, zohladnovatNormaly, vyskaUseku);
		this.vykonavaSaUlohaAStav = vykonavaSaUloha;
	}

	@Override
	public void run() {
		ExecutorService exekutor = Executors.newSingleThreadExecutor();
		Future<Double> f = exekutor.submit(uloha);

		vykonavaSaUlohaAStav.setVysledokVykonania(false);
		try {
			vykonavaSaUlohaAStav.setObjem(f.get());
			vykonavaSaUlohaAStav.setPrebiehaVykonavanie(false);
		} catch (InterruptedException | ExecutionException e1) {
			// ked sa prerusi nacitanie, napr. tlacitkom
			vykonavaSaUlohaAStav.setObjem(-1);
			vykonavaSaUlohaAStav.setPrebiehaVykonavanie(false);
		}
	}

	

}
