package sk.upjs.entity;

/**
 * Uchovava vsetky informacie z nacitaneho suboru ply, ktore su potrebne pre
 * fungovanie algoritmov alebo zapis do vystupneho suboru ply
 *
 */
public class NacitanySubor {

	/**
	 * Hlavicka povodneho suboru ply z ktoreho su nacitane vrcholy
	 */
	private final String hlavickaSuboru;

	/**
	 * Pole vsetkych vrcholov 3D modelu, ktore boly v nacitanom subore
	 */
	private final Vertex[] vrcholy;

	/**
	 * Index vrcholu z nacitaneho suboru, ktory ma najvacsiu suradnicu Z zo vsetkych vrcholov
	 */
	private final int idxGlobalnehoMaxima;
	
	/**
	 * Index vrcholu z nacitaneho suboru, ktory ma najmensiu suradnicu Z zo vsetkych vrcholov
	 */
	private final int idxGlobalnehoMinima;

	public NacitanySubor(String hlavickaSuboru, Vertex[] vrcholy, int idxGlobalnehoMaxima, int idxGlobalnehoMinima) {
		this.hlavickaSuboru = hlavickaSuboru;
		this.vrcholy = vrcholy;
		this.idxGlobalnehoMaxima = idxGlobalnehoMaxima;
		this.idxGlobalnehoMinima = idxGlobalnehoMinima;
	}

	public String getHlavickaSuboru() {
		return hlavickaSuboru;
	}

	public Vertex getVrchol(int index) {
		if (index < 0 || index >= vrcholy.length) {
			return null;
		}
		return vrcholy[index];
	}

	public int getPocetVrcholov() {
		return vrcholy.length;
	}

	public int getIdxGlobalnehoMaxima() {
		return idxGlobalnehoMaxima;
	}

	public int getIdxGlobalnehoMinima() {
		return idxGlobalnehoMinima;
	}
}
