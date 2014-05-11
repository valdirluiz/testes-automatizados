package br.com.caelum.leilao.dominio;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.leilao.builder.CriadorDeLeilao;

public class LeilaoTest {
	
	private Usuario steveJobs;
	private Usuario steveWozniak;
	private Usuario billGates;

	@Before
	public void setUp() {
		steveJobs = new Usuario("Steve Jobs");
		steveWozniak = new Usuario("Steve Wozniak");
		billGates = new Usuario("Bill Gates");
	}
	

	@Test
	public void deveReceberUmLance() {
		Leilao leilao = new Leilao("Macbook Pro 15");
		
		assertThat(leilao.getLances().size(), equalTo(0));
		
		leilao.propoe(new Lance(new Usuario("Steve Jobs"), 3000));

		assertThat(leilao.getLances().size(), equalTo(1));
		assertThat(leilao.getLances().get(0).getValor(), is(3000.0));
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
		Leilao leilao = new CriadorDeLeilao().para("Macbook Pro 15")
		.lance(steveJobs, 2000)
		.lance(steveJobs, 3000)
		.constroi();
		
		assertThat(leilao.getLances().size(), equalTo(1));
		assertThat(leilao.getLances(), hasItem(
				new Lance(steveJobs, 2000)
		));
		assertThat(leilao.getLances(), not(hasItem(
				new Lance(steveJobs, 3000)
		)));
	}

	@Test
	public void naoDeveAceitarMaisDe5LancesDeUmMesmoUsuario() {
		
		Leilao leilao = new CriadorDeLeilao().para("Macbook Pro 15")
		.lance(steveJobs, 2000)
		.lance(billGates, 3000)
		.lance(steveJobs, 4000)
		.lance(billGates, 5000)
		.lance(steveJobs, 6000)
		.lance(billGates, 7000)
		.lance(steveJobs, 8000)
		.lance(billGates, 9000)
		.lance(steveJobs, 10000)
		.lance(billGates, 11000.0)
		.lance(steveJobs, 12000)
		.constroi();
		
		assertThat(leilao.getLances().size(), equalTo(10));
		assertThat(leilao.getLances().get(leilao.getLances().size()-1).getValor(), is(11000.0));

	}

	@Test
	public void deveDobrarOUltimoLanceDado() {
		Leilao leilao = new CriadorDeLeilao().para("Macbook Pro 15")
		.lance(steveJobs, 2000)
		.lance(billGates, 3000)
		.constroi();
		
		leilao.dobraLance(steveJobs);
		
		assertThat(leilao.getLances(), hasItem(
				new Lance(steveJobs, 4000)
		));
		
	}

	@Test
	public void naoDeveDobrarCasoNaoHajaLanceAnterior() {
		Leilao leilao = new CriadorDeLeilao().para("Macbook Pro 15").constroi();
	
		leilao.dobraLance(steveJobs);

		assertThat(leilao.getLances().size(), equalTo(0));
	}

}
