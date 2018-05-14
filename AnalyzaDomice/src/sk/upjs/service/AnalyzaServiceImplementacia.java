package sk.upjs.service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import sk.upjs.entity.NacitanySubor;
import sk.upjs.entity.Vertex;
import sk.upjs.pracaSDatami.Algoritmy;
import sk.upjs.pracaSDatami.AlgoritmyImplementacia;
import sk.upjs.pracaSDatami.VyrobcaANacitavacSuboruPLY;

/**
 * Pre spravne fungovanie treba spustit inicializaciu
 *
 */
public class AnalyzaServiceImplementacia implements AnalyzaService {

	private final File suborPLY;
	private NacitanySubor nacitanySubor;
	private Algoritmy algoritmy;

	public AnalyzaServiceImplementacia(File suborPLY) {
		this.suborPLY = suborPLY;
	}

	/**
	 * @return ci sa podarila inicializacia
	 */
	public boolean inicializacia() {
		this.nacitanySubor = VyrobcaANacitavacSuboruPLY.nacitajSubor(suborPLY);
		this.algoritmy = new AlgoritmyImplementacia(nacitanySubor);
		return !(nacitanySubor == null);
	}

	public boolean isNacitanieSuboruOK() {
		return !(nacitanySubor == null);
	}

	public int idxGlobalnehoMax() {		
		return nacitanySubor.getIdxGlobalnehoMaxima();
	}

	public int idxGlobalnehoMin() {
		return nacitanySubor.getIdxGlobalnehoMinima();
	}

	public boolean vyrobSuborSLokalnymiMinimamiCeleho3Dmodelu(String cielovyAdresar, String nazovSuboru,
			boolean zohladnovatNormaly) {
		if (nacitanySubor == null || cielovyAdresar == null) {
			return false;
		}

		boolean[] minimaBoo = algoritmy.lokalneMinimaCeleho(zohladnovatNormaly);
		List<Vertex> minima = new ArrayList<>();

		for (int i = 0; i < minimaBoo.length; i++) {
			if (minimaBoo[i]) {
				minima.add(nacitanySubor.getVrchol(i));
			}
		}

		return VyrobcaANacitavacSuboruPLY.vyrobSuborPLY(nacitanySubor.getHlavickaSuboru(), cielovyAdresar, nazovSuboru,
				minima, false, "Minima z celeho vstupneho 3D modelu, boli zohladnovane normaly: " + zohladnovatNormaly);

	}

	public boolean vsetkyStalaktityPoPrveRozvetvenieDoSuboru(String cielovyAdresar, String nazovSuboru,
			boolean zohladnovatNormaly, boolean doJednehoSuboru, boolean ajOsamoteneBody) {

		boolean[] minimaBoo = algoritmy.lokalneMinimaCeleho(zohladnovatNormaly);

		Set<List<Vertex>> stalaktity = new HashSet<>();

		for (int i = 0; i < minimaBoo.length; i++) {
			if (minimaBoo[i]) {
				List<Vertex> stalaktit = algoritmy.jedenStalaktitPoPrveRozvetvenie(i);

				if (stalaktit.size() < 3) {
					// stalaktit tvoreny len jednym bodom sa zoberie len ak je to zadane v parametri
					if (ajOsamoteneBody) {
						stalaktity.add(stalaktit);
					}
				} else {
					stalaktity.add(stalaktit);
				}
			}
		}

		return VyrobcaANacitavacSuboruPLY.vyrobSuborPLY(nacitanySubor.getHlavickaSuboru(), cielovyAdresar, nazovSuboru,
				stalaktity, true,
				"Stalaktity po prve rozvetvenie zo vsetkych minim 3D modelu; Pocet vsetkych: " + stalaktity.size(),
				doJednehoSuboru);

	}

	public boolean vsetkyStalaktityPoPrveRozvetvenieDoSuboru(String cielovyAdresar, String nazovSuboru,
			boolean zohladnovatNormaly, boolean doJednehoSuboru, boolean ajOsamoteneBody, float uholMaxOdchylky,
			int idxVrcholuZoStropu) {

		boolean[] lokalneMinimaCeleho = algoritmy.lokalneMinimaCeleho(zohladnovatNormaly);
		boolean[] stropAStalaktity = algoritmy.oddelitStropAStalaktity(uholMaxOdchylky, idxVrcholuZoStropu);
		List<Vertex> minima = algoritmy.minimaNepatriaceStropu(lokalneMinimaCeleho, stropAStalaktity);

		Set<List<Vertex>> stalaktity = new HashSet<>();

		for (Vertex vertex : minima) {
			List<Vertex> stalaktit = algoritmy.jedenStalaktitPoPrveRozvetvenie(vertex.getIdxZPLY());

			if (stalaktit.size() < 3) {
				// stalaktit tvoreny len jednym bodom sa zoberie len ak je to zadane v parametri
				if (ajOsamoteneBody) {
					stalaktity.add(stalaktit);
				}
			} else {
				stalaktity.add(stalaktit);
			}
		}

		return VyrobcaANacitavacSuboruPLY.vyrobSuborPLY(nacitanySubor.getHlavickaSuboru(), cielovyAdresar, nazovSuboru,
				stalaktity, true,
				"Pocet stalaktitov z minim nepatriacich stropu: " + stalaktity.size() + "; uhol stropu: " + uholMaxOdchylky,
				doJednehoSuboru);

	}

	public boolean vyrobSuborSoStalaktitmiPoOddeleniStropu(String cielovyAdresar, String nazovSuboru, float uholMaxOdchylky,
			int idxVrcholuZoStropu, boolean ajSuborSOddelenymStropom, boolean doJednehoSuboru,
			boolean ajOsamoteneBody) {

		if (idxVrcholuZoStropu >= nacitanySubor.getPocetVrcholov() || idxVrcholuZoStropu < 0) {
			return false;
		}

		boolean[] stropAStalaktity = algoritmy.oddelitStropAStalaktity(uholMaxOdchylky, idxVrcholuZoStropu);

		// jednoduchsi pripad ked nehladame komponenty suvislosti
		// -----------------------------------------------------------
		if (doJednehoSuboru && ajOsamoteneBody) {
			// list vrcholov, ktore boli vyhodnotene, ze patria stropu
			ArrayList<Vertex> strop = new ArrayList<>();
			// list vrcholov, ktore by mali patrit stalaktitom
			List<Vertex> stalaktitySpolu = new ArrayList<>();

			for (int i = 0; i < stropAStalaktity.length; i++) {
				if (stropAStalaktity[i]) {
					if (ajSuborSOddelenymStropom) {
						strop.add(nacitanySubor.getVrchol(i));
					}
				} else {
					stalaktitySpolu.add(nacitanySubor.getVrchol(i));
				}
			}

			// vyrobenie suboru s stalaktitmi
			boolean okSuborSoStalaktitmi = VyrobcaANacitavacSuboruPLY.vyrobSuborPLY(nacitanySubor.getHlavickaSuboru(),
					cielovyAdresar, nazovSuboru, stalaktitySpolu, true,
					"Stalaktity co ostali po oddeleni stropu do sklonu " + uholMaxOdchylky);

			// tu koncime ak netreba subor so stropom
			if (!ajSuborSOddelenymStropom) {
				return okSuborSoStalaktitmi;
			}

			// vyrobenie suboru so stropom
			return VyrobcaANacitavacSuboruPLY.vyrobSuborPLY(nacitanySubor.getHlavickaSuboru(), cielovyAdresar,
					nazovSuboru + "_strop", strop, true, "Strop do sklonu " + uholMaxOdchylky) && okSuborSoStalaktitmi;
		}

		// ked nam treba poznat komponenty suvislosti
		// -----------------------------------------------------------

		// list vrcholov, ktore boli vyhodnotene, ze patria stropu
		ArrayList<Vertex> strop = new ArrayList<>();
		// list vrcholov, ktore by mali patrit stalaktitom
		Set<List<Vertex>> stalaktityVsetky = algoritmy.komoponentySuvislosti(stropAStalaktity);

		if (ajSuborSOddelenymStropom) {
			for (int i = 0; i < stropAStalaktity.length; i++) {
				if (stropAStalaktity[i]) {
					strop.add(nacitanySubor.getVrchol(i));
				}
			}
		}

		// stalaktit tvoreny len jednym bodom sa zoberie len ak je to zadane v parametri
		if (!ajOsamoteneBody) {

			Set<List<Vertex>> stalaktityBezOsamostatnenych = new HashSet<>();
			for (List<Vertex> stalaktit : stalaktityVsetky) {
				if (stalaktit.size() >= 3) {
					stalaktityBezOsamostatnenych.add(stalaktit);
				}
			}
			stalaktityVsetky = stalaktityBezOsamostatnenych;
		}

		String comment = "Stalaktity co ostali po oddeleni stropu do sklonu " + uholMaxOdchylky;

		if (!ajOsamoteneBody && doJednehoSuboru) {
			comment += "; bez osamostatnenych vrcholov";
		}

		// ci sa podarilo vyrobit subory s stalaktitmi
		boolean okSuborySoStalaktitmi = VyrobcaANacitavacSuboruPLY.vyrobSuborPLY(nacitanySubor.getHlavickaSuboru(),
				cielovyAdresar, nazovSuboru, stalaktityVsetky, true, comment, doJednehoSuboru);

		// tu koncime ak netreba subor so stropom
		if (!ajSuborSOddelenymStropom) {
			return okSuborySoStalaktitmi;
		}

		// vyrobenie suboru so stropom
		return VyrobcaANacitavacSuboruPLY.vyrobSuborPLY(nacitanySubor.getHlavickaSuboru(), cielovyAdresar,
				nazovSuboru + "_strop", strop, true, "Strop do sklonu " + uholMaxOdchylky) && okSuborySoStalaktitmi;
	}

	public boolean vyrobSuborSMinimamiKtoreNepatriaStropu(String cielovyAdresar, String nazovSuboru,
			float uholMaxOdchylky, int idxVrcholuZoStropu, boolean zohladnovatNormaly) {

		boolean[] lokalneMinimaCeleho = algoritmy.lokalneMinimaCeleho(zohladnovatNormaly);
		boolean[] stropAStalaktity = algoritmy.oddelitStropAStalaktity(uholMaxOdchylky, idxVrcholuZoStropu);

		List<Vertex> minima = algoritmy.minimaNepatriaceStropu(lokalneMinimaCeleho, stropAStalaktity);

		return VyrobcaANacitavacSuboruPLY.vyrobSuborPLY(nacitanySubor.getHlavickaSuboru(), cielovyAdresar, nazovSuboru,
				minima, false, "Minima stalaktitov co ostali po oddeleno stropu s odchylkou do " + uholMaxOdchylky);

	}

	public boolean vyrobSuborySKomponentmiSuvislosti(String cielovyAdresar, String nazovSuboru,
			boolean ajOsamoteneBody) {

		Set<List<Vertex>> komponentySuvislosti = algoritmy
				.komoponentySuvislosti(new boolean[nacitanySubor.getPocetVrcholov()]);

		if (ajOsamoteneBody) {
			return VyrobcaANacitavacSuboruPLY.vyrobSuborPLY(nacitanySubor.getHlavickaSuboru(), cielovyAdresar,
					nazovSuboru, komponentySuvislosti, true, "Suvisla cast povodneho 3D modelu ", false);
		}

		// ked sa to dostalo tu, tak z mnoziny vyberieme len tie komponenty suvislosti,
		// ktore nie su tvorene len bodmi

		Set<List<Vertex>> ostavajuceKomponentySuvislosti = new HashSet<>();
		for (List<Vertex> komponentSuvislosti : komponentySuvislosti) {
			if (komponentSuvislosti.size() >= 3) {
				ostavajuceKomponentySuvislosti.add(komponentSuvislosti);
			}
		}
		return VyrobcaANacitavacSuboruPLY.vyrobSuborPLY(nacitanySubor.getHlavickaSuboru(), cielovyAdresar, nazovSuboru,
				ostavajuceKomponentySuvislosti, true, "Suvisla cast povodneho 3D modelu ", false);

	}

	public boolean vyrobSuborSVyseparovanimiStalaktitmi(String cielovyAdresar, String nazovSuboru, float uholMaxOdchylky,
			int idxVrcholuZoStropu, boolean zohladnovatNormaly, boolean ajSuborSOddelenymStropom,
			boolean doJednehoSuboru, boolean ajOsamoteneBody) {

		if (idxVrcholuZoStropu >= nacitanySubor.getPocetVrcholov() || idxVrcholuZoStropu < 0) {
			return false;
		}

		boolean[] lokalneMinimaCeleho = algoritmy.lokalneMinimaCeleho(zohladnovatNormaly);
		boolean[] stropAStalaktity = algoritmy.oddelitStropAStalaktity(uholMaxOdchylky, idxVrcholuZoStropu);
		List<Vertex> minimaNepatriaceStropu = algoritmy.minimaNepatriaceStropu(lokalneMinimaCeleho, stropAStalaktity);

		boolean[] vyseparovaneStalaktityBoo = algoritmy.vyseparovanieStalaktitov(minimaNepatriaceStropu, stropAStalaktity);

		// jednoduchsi pripad ked nehladame komponenty suvislosti
		// -----------------------------------------------------------
		if (doJednehoSuboru && ajOsamoteneBody) {
			// list vrcholov, ktore boli vyhodnotene, ze patria stropu
			ArrayList<Vertex> strop = new ArrayList<>();
			// list vrcholov, ktore by mali patrit stalaktitom
			List<Vertex> stalaktity = new ArrayList<>();

			for (int i = 0; i < vyseparovaneStalaktityBoo.length; i++) {
				if (vyseparovaneStalaktityBoo[i]) {
					if (ajSuborSOddelenymStropom) {
						strop.add(nacitanySubor.getVrchol(i));
					}
				} else {
					stalaktity.add(nacitanySubor.getVrchol(i));
				}
			}

			// vyrobenie suboru s stalaktitmi
			boolean okSuborSoStalaktitmi = VyrobcaANacitavacSuboruPLY.vyrobSuborPLY(nacitanySubor.getHlavickaSuboru(),
					cielovyAdresar, nazovSuboru, stalaktity, true,
					"Vyseparované stalaktity co ostali po oddeleni stropu do sklonu " + uholMaxOdchylky);

			// tu koncime ak netreba subor so stropom
			if (!ajSuborSOddelenymStropom) {
				return okSuborSoStalaktitmi;
			}

			// vyrobenie suboru so stropom
			return VyrobcaANacitavacSuboruPLY.vyrobSuborPLY(nacitanySubor.getHlavickaSuboru(), cielovyAdresar,
					nazovSuboru + "_strop", strop, true, "Strop po vyseparovanych stalaktitoch do sklonu " + uholMaxOdchylky)
					&& okSuborSoStalaktitmi;
		}

		// ked nam treba poznat komponenty suvislosti
		// -----------------------------------------------------------

		// list vrcholov, ktore boli vyhodnotene, ze patria stropu
		ArrayList<Vertex> strop = new ArrayList<>();
		// list vrcholov, ktore by mali patrit stalaktitom
		Set<List<Vertex>> stalaktityVsetky = algoritmy.komoponentySuvislosti(vyseparovaneStalaktityBoo);

		if (ajSuborSOddelenymStropom) {
			for (int i = 0; i < vyseparovaneStalaktityBoo.length; i++) {
				if (vyseparovaneStalaktityBoo[i]) {
					strop.add(nacitanySubor.getVrchol(i));
				}
			}
		}

		// stalaktit tvoreny len jednym bodom sa zoberie len ak je to zadane v parametri
		if (!ajOsamoteneBody) {

			Set<List<Vertex>> stalaktityBezOsamostatnenych = new HashSet<>();
			for (List<Vertex> stalaktit : stalaktityVsetky) {
				if (stalaktit.size() >= 3) {
					stalaktityBezOsamostatnenych.add(stalaktit);
				}
			}
			stalaktityVsetky = stalaktityBezOsamostatnenych;
		}

		String comment = "Vyseparované stalaktity co ostali po oddeleni stropu do sklonu " + uholMaxOdchylky;

		if (!ajOsamoteneBody && doJednehoSuboru) {
			comment += "; bez osamostatnenych vrcholov";
		}

		// ci sa podarilo vyrobit subory s stalaktitmi
		boolean okSuborySoStalaktitmi = VyrobcaANacitavacSuboruPLY.vyrobSuborPLY(nacitanySubor.getHlavickaSuboru(),
				cielovyAdresar, nazovSuboru, stalaktityVsetky, true, comment, doJednehoSuboru);

		// tu koncime ak netreba subor so stropom
		if (!ajSuborSOddelenymStropom) {
			return okSuborySoStalaktitmi;
		}

		// vyrobenie suboru so stropom
		return VyrobcaANacitavacSuboruPLY.vyrobSuborPLY(nacitanySubor.getHlavickaSuboru(), cielovyAdresar,
				nazovSuboru + "_strop", strop, true, "Strop po vyseparovanych stalaktitoch do sklonu " + uholMaxOdchylky)
				&& okSuborySoStalaktitmi;

	}

	public double objemVyseparovanychStalaktitovZCeleho3DModelu(float uholMaxOdchylky, int idxVrcholuZoStropu, float vyskaUseku,
			boolean zohladnovatNormaly) {

		boolean[] stropAStalaktity = algoritmy.oddelitStropAStalaktity(uholMaxOdchylky, idxVrcholuZoStropu);
		boolean[] lokalneMinimaCeleho = algoritmy.lokalneMinimaCeleho(zohladnovatNormaly);

		// toto su zaroven minima vyseparovanych stalaktitov, kedze z nich sa vyseparovane stalaktity
		// hladali
		List<Vertex> minimaNepatriaceStropu = algoritmy.minimaNepatriaceStropu(lokalneMinimaCeleho, stropAStalaktity);

		boolean[] stropAVyseparovaneStalaktity = algoritmy.vyseparovanieStalaktitov(minimaNepatriaceStropu, stropAStalaktity);

		return algoritmy.objemStalaktitov(stropAVyseparovaneStalaktity, minimaNepatriaceStropu, vyskaUseku);

	}

	public double objemStalaktitovZoSuboruLenSoStalaktitmi(float vyskaUseku, boolean zohladnovatNormaly) {

		boolean[] lokalneMinimaCeleho = algoritmy.lokalneMinimaCeleho(zohladnovatNormaly);
		List<Vertex> minimaZadanychStalaktitov = new ArrayList<>();

		for (int i = 0; i < lokalneMinimaCeleho.length; i++) {
			if (lokalneMinimaCeleho[i]) {
				minimaZadanychStalaktitov.add(nacitanySubor.getVrchol(i));
			}
		}

		return algoritmy.objemStalaktitov(new boolean[nacitanySubor.getPocetVrcholov()], minimaZadanychStalaktitov, vyskaUseku);
	}

}
