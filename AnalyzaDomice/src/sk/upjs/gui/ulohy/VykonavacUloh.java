package sk.upjs.gui.ulohy;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import sk.upjs.gui.VykonavaSaUlohaAStav;
import sk.upjs.service.AnalyzaServiceImplementacia;

/**
 * Rozne konstruktory vyrobia vzdy ulohu pre vykonanie jedneho algoritmu a ked
 * sa tato trieda spusti v osobitnom vlakne tak sa vyrobena uloha spusti v
 * osobitnom vlakne, ktore ked dobehne a vrati vysledok tak ten je zapisany do
 * zdielanej triedy VykonavaSaUlohaAStav aby okno aplikacie vedelo zistit, ze
 * uloha dobehla a ako skoncila
 */
public class VykonavacUloh implements Runnable {

	private VypocetUloha uloha;
	private VykonavaSaUlohaAStav vykonavaSaUlohaAStav;

	// --------------------------------------------------------
	// konstruktory, kde kazdy konstruktor zodpoveda obsluhe jedmeho algoritmu

	// Inicializacia
	public VykonavacUloh(VykonavaSaUlohaAStav vykonavaSaUloha, AnalyzaServiceImplementacia service) {
		uloha = new InicializaciaUloha(service);
		this.vykonavaSaUlohaAStav = vykonavaSaUloha;
	}

	// LokaleMinimaCeleho3Dmodelu, KomponentySuvislosti
	public VykonavacUloh(int cisloAlgoritmu, VykonavaSaUlohaAStav vykonavaSaUlohaAStav,
			AnalyzaServiceImplementacia service, String cielovyAdresar, String nazovSuboru,
			boolean zohladnovatNormaly_ajOsamoteneBody) {
		this.vykonavaSaUlohaAStav = vykonavaSaUlohaAStav;

		if (cisloAlgoritmu == 0) {
			this.uloha = new LokaleMinimaCeleho3DmodeluUloha(service, cielovyAdresar, nazovSuboru,
					zohladnovatNormaly_ajOsamoteneBody);
		}
		if (cisloAlgoritmu == 5) {
			this.uloha = new KomponentySuvislostiUloha(service, cielovyAdresar, nazovSuboru,
					zohladnovatNormaly_ajOsamoteneBody);
		}
	}

	// StalaktityPoRozvetvenieZoVsetkych
	public VykonavacUloh(int cisloAlgoritmu, VykonavaSaUlohaAStav vykonavaSaUloha, AnalyzaServiceImplementacia service,
			String cielovyAdresar, String nazovSuboru, boolean zohladnovatNormaly, boolean doJednehoSuboru,
			boolean ajOsamoteneBody) {
		this.vykonavaSaUlohaAStav = vykonavaSaUloha;

		if (cisloAlgoritmu == 1) {
			this.uloha = new StalaktityPoRozvetvenieZoVsetkychUloha(service, cielovyAdresar, nazovSuboru,
					zohladnovatNormaly, doJednehoSuboru, ajOsamoteneBody);
		}

	}

	// StalaktityPoRozvetvenieZNepatriacichStropu
	public VykonavacUloh(int cisloAlgoritmu, VykonavaSaUlohaAStav vykonavaSaUloha, AnalyzaServiceImplementacia service,
			String cielovyAdresar, String nazovSuboru, boolean zohladnovatNormaly, boolean doJednehoSuboru,
			boolean ajOsamoteneBody, float uholMaxOdchylky, int idxVrcholuZoStropu) {
		this.vykonavaSaUlohaAStav = vykonavaSaUloha;

		if (cisloAlgoritmu == 2) {
			this.uloha = new StalaktityPoRozvetvenieZNepatriacichStropuUloha(service, cielovyAdresar, nazovSuboru,
					zohladnovatNormaly, doJednehoSuboru, ajOsamoteneBody, uholMaxOdchylky, idxVrcholuZoStropu);
		}
	}

	// StalaktityPoOddeleniStropu
	public VykonavacUloh(int cisloAlgoritmu, VykonavaSaUlohaAStav vykonavaSaUloha, AnalyzaServiceImplementacia service,
			String cielovyAdresar, String nazovSuboru, float uholMaxOdchylky, int idxVrcholuZoStropu,
			boolean ajSuborSOddelenymStropom, boolean doJednehoSuboru, boolean ajOsamoteneBody) {
		this.vykonavaSaUlohaAStav = vykonavaSaUloha;

		if (cisloAlgoritmu == 3) {
			this.uloha = new StalaktityPoOddeleniStropuUloha(service, cielovyAdresar, nazovSuboru, uholMaxOdchylky,
					idxVrcholuZoStropu, ajSuborSOddelenymStropom, doJednehoSuboru, ajOsamoteneBody);
		}
	}

	// LokalneMinimaNepatriaceStropu
	public VykonavacUloh(int cisloAlgoritmu, VykonavaSaUlohaAStav vykonavaSaUloha, AnalyzaServiceImplementacia service,
			String cielovyAdresar, String nazovSuboru, float uholMaxOdchylky, int idxVrcholuZoStropu,
			boolean zohladnovatNormaly) {
		this.vykonavaSaUlohaAStav = vykonavaSaUloha;

		if (cisloAlgoritmu == 4) {
			this.uloha = new LokalneMinimaNepatriaceStropuUloha(service, cielovyAdresar, nazovSuboru, uholMaxOdchylky,
					idxVrcholuZoStropu, zohladnovatNormaly);
		}
	}

	// VyseparovanieStalaktitov
	public VykonavacUloh(int cisloAlgoritmu, VykonavaSaUlohaAStav vykonavaSaUloha, AnalyzaServiceImplementacia service,
			String cielovyAdresar, String nazovSuboru, float uholMaxOdchylky, int idxVrcholuZoStropu,
			boolean zohladnovatNormaly, boolean ajSuborSOddelenymStropom, boolean doJednehoSuboru,
			boolean ajOsamoteneBody) {
		this.vykonavaSaUlohaAStav = vykonavaSaUloha;

		if (cisloAlgoritmu == 6) {
			this.uloha = new VyseparovanieStalaktitovUloha(service, cielovyAdresar, nazovSuboru, uholMaxOdchylky,
					idxVrcholuZoStropu, zohladnovatNormaly, ajSuborSOddelenymStropom, doJednehoSuboru, ajOsamoteneBody);
		}
	}

	@Override
	public void run() {
		ExecutorService exekutor = Executors.newSingleThreadExecutor();
		Future<Boolean> f = exekutor.submit(uloha);

		vykonavaSaUlohaAStav.setVysledokVykonania(false);
		try {
			vykonavaSaUlohaAStav.setVysledokVykonania(f.get());
			vykonavaSaUlohaAStav.setPrebiehaVykonavanie(false);
		} catch (InterruptedException | ExecutionException e1) {
			// ked sa prerusi nacitanie, napr. tlacitkom
			vykonavaSaUlohaAStav.setVysledokVykonania(false);
			vykonavaSaUlohaAStav.setPrebiehaVykonavanie(false);
		}
	}

}
