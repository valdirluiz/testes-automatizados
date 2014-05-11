package br.com.caelum.leilao.dominio;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

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
		Lance lance = new Lance(usuario, 0.001);
		assertThat(lance.getValor(), is(0.001));
		
	}

}
