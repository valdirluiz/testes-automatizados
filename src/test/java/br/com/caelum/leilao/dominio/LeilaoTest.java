package br.com.caelum.leilao.dominio;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.hamcrest.Matchers.*;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.leilao.builder.CriadorDeLeilao;
import br.com.caelum.leilao.servico.Avaliador;

public class LeilaoTest {
	
	private Usuario steveJobs;
	private Usuario steveWozniak;

	@Before
	public void setUp() {
		steveJobs = new Usuario("Steve Jobs");
		steveWozniak = new Usuario("Steve Wozniak");
	}
	

	@Test
	public void deveReceberUmLance() {
		Leilao leilao = new Leilao("Macbook Pro 15");
		assertEquals(0, leilao.getLances().size());

		leilao.propoe(new Lance(new Usuario("Steve Jobs"), 3000));

		assertEquals(1, leilao.getLances().size());
		assertEquals(3000.0, leilao.getLances().get(0).getValor(), 0.00001);
	}

	@Test
	public void deveReceberVariosLances() {
		
		Leilao leilao = new CriadorDeLeilao().para("Macbook Pro 15")
		.lance(steveJobs, 2000)
		.lance(steveWozniak, 3000)
		.constroi();

		assertThat(leilao.getLances().size(), equalTo(2));
		assertThat(leilao.getLances(), hasItems(
				new Lance(steveJobs, 2000),
				new Lance(steveWozniak, 3000)
		));
		
	}

	@Test
	public void naoDeveAceitarDoisLancesSeguidosDoMesmoUsuario() {
		Leilao leilao = new Leilao("Macbook Pro 15");
		Usuario steveJobs = new Usuario("Steve Jobs");

		leilao.propoe(new Lance(steveJobs, 2000.0));
		leilao.propoe(new Lance(steveJobs, 3000.0));

		assertEquals(1, leilao.getLances().size());
		assertEquals(2000.0, leilao.getLances().get(0).getValor(), 0.00001);
	}

	@Test
	public void naoDeveAceitarMaisDe5LancesDeUmMesmoUsuario() {
		Leilao leilao = new Leilao("Macbook Pro 15");

		Usuario steveJobs = new Usuario("Steve Jobs");
		Usuario billGates = new Usuario("Bill Gates");

		leilao.propoe(new Lance(steveJobs, 2000));
		leilao.propoe(new Lance(billGates, 3000));

		leilao.propoe(new Lance(steveJobs, 4000));
		leilao.propoe(new Lance(billGates, 5000));

		leilao.propoe(new Lance(steveJobs, 6000));
		leilao.propoe(new Lance(billGates, 7000));

		leilao.propoe(new Lance(steveJobs, 8000));
		leilao.propoe(new Lance(billGates, 9000));

		leilao.propoe(new Lance(steveJobs, 10000));
		leilao.propoe(new Lance(billGates, 11000));

		// deve ser ignorado
		leilao.propoe(new Lance(steveJobs, 12000));

		assertEquals(10, leilao.getLances().size());
		assertEquals(11000.0,
				leilao.getLances().get(leilao.getLances().size() - 1)
						.getValor(), 0.0001);

	}

	@Test
	public void deveDobrarOUltimoLanceDado() {
		Leilao leilao = new Leilao("Macbook Pro 15");
		Usuario steveJobs = new Usuario("Steve Jobs");
		Usuario billGates = new Usuario("Bill Gates");

		leilao.propoe(new Lance(steveJobs, 2000));
		leilao.propoe(new Lance(billGates, 3000));
		leilao.dobraLance(steveJobs);

		assertEquals(4000, leilao.getLances().get(2).getValor(), 0.00001);
	}

	@Test
	public void naoDeveDobrarCasoNaoHajaLanceAnterior() {
		Leilao leilao = new Leilao("Macbook Pro 15");
		Usuario steveJobs = new Usuario("Steve Jobs");

		leilao.dobraLance(steveJobs);

		assertEquals(0, leilao.getLances().size());
	}

}
