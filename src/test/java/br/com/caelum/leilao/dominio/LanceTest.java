package br.com.caelum.leilao.dominio;

import org.junit.Before;
import org.junit.Test;

public class LanceTest {

	private Usuario usuario;

	@Before
	public void setUp() {
		usuario = new Usuario("usuario");
	}

	@Test(expected = IllegalArgumentException.class)
	public void naoDeveAceitarValoresIguaisAZeroComoLance() {
		new Lance(usuario, 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void naoDeveAceitarValoresNegativosComoLance() {
		new Lance(usuario, -1);
	}

	@Test
	public void deveAceitarValoresMaioresQueZeroComoLance() {
		new Lance(usuario, 0.001);
	}

}
