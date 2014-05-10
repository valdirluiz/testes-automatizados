package br.com.caelum.leilao.servico;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.hamcrest.Matchers.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.leilao.builder.CriadorDeLeilao;
import br.com.caelum.leilao.dominio.Lance;
import br.com.caelum.leilao.dominio.Leilao;
import br.com.caelum.leilao.dominio.Usuario;

public class AvaliadorTeste {

	private Avaliador leiloeiro;
	private Usuario joao;
	private Usuario maria;
	private Usuario jose;


	@Before
	public void setUp() {
		this.leiloeiro = new Avaliador();
		this.joao = new Usuario("João");
		this.maria = new Usuario("Maria");
		this.jose = new Usuario("José");
	}
	
	@Test
	public void deveEntenderLancesEmOrdemAleatoria() {
		Leilao leilao = new CriadorDeLeilao().para("Playstation 3 Novo")
        .lance(joao, 300)
        .lance(jose, 2500)
        .lance(maria, 100)
        .constroi();

		leiloeiro.avalia(leilao);

		assertEquals(2500, leiloeiro.getMaiorLance(), 0.0);
		assertEquals(100, leiloeiro.getMenorLance(), 0.0);

	}

	@Test
	public void deveEntenderLancesEmOrdemCrescente() {
		Leilao leilao = new CriadorDeLeilao().para("Playstation 3 Novo")
        .lance(joao, 250)
        .lance(jose, 300.0)
        .lance(maria, 400.0)
        .constroi();

		leiloeiro.avalia(leilao);

		assertEquals(400, leiloeiro.getMaiorLance(), 0.0);
		assertEquals(250, leiloeiro.getMenorLance(), 0.0);

	}
	
	@Test
	public void deveEntenderLancesEmOrdemDecrescente() {
		Leilao leilao = new CriadorDeLeilao().para("Playstation 3 Novo")
        .lance(joao, 400)
        .lance(jose, 300.0)
        .lance(maria, 250.0)
        .lance(jose, 100)
        .constroi();

		leiloeiro.avalia(leilao);
		
	    assertThat(leiloeiro.getMenorLance(), equalTo(100.0));
	    assertThat(leiloeiro.getMaiorLance(), equalTo(400.0));

	}
	
	@Test
	public void deveEntenderLanceMedioEmOrdemCrescente() {
		Leilao leilao = new CriadorDeLeilao().para("Playstation 3 Novo")
        .lance(joao, 250)
        .lance(jose, 300.0)
        .lance(maria, 400.0)
        .constroi();

		leiloeiro.avalia(leilao);
		assertEquals(316.666666667, leiloeiro.getValorMedioDosLances(), 0.0001);

	}
	
	@Test
	public void deveEntenderLanceMedioEmOrdemDecrescente() {
		Leilao leilao = new CriadorDeLeilao().para("Playstation 3 Novo")
		.lance(joao, 400)
		.lance(jose, 300)
		.lance(maria, 250)
		.constroi();
		
		leiloeiro.avalia(leilao);
		assertEquals(316.666666667, leiloeiro.getValorMedioDosLances(), 0.0001);

	}
	
	@Test
	public void deveEntenderLeiaoComApenasUmLance(){
		 Leilao leilao = new CriadorDeLeilao().para("Playstation 3 Novo")
        .lance(joao, 1000)
        .constroi();
	
		leiloeiro.avalia(leilao);
		
		assertEquals(1000, leiloeiro.getMaiorLance(),0.0001);
		assertEquals(1000, leiloeiro.getMenorLance(),0.0001);
		assertEquals(1000, leiloeiro.getValorMedioDosLances(),0.0001);
	}
	
	@Test
	public void deveEncontrarOsTresMaioresLances(){		
		Leilao leilao = new CriadorDeLeilao().para("Playstation 3 Novo")
        .lance(joao, 100.0)
        .lance(maria, 200.0)
        .lance(joao, 300.0)
        .lance(maria, 400.0)
        .constroi();

		leiloeiro.avalia(leilao);
		
		List<Lance> tresMaiores = leiloeiro.getTresMaiores();
		
		assertEquals(3, tresMaiores.size());
		assertThat(tresMaiores, hasItems(
				new Lance(maria, 400),
				new Lance(joao, 300),
				new Lance(maria, 200)
		));
	}

	@Test(expected = RuntimeException.class)
	public void naoDeveAvaliarLeilaoSemLances() {		
		Leilao leilao = new CriadorDeLeilao().para("Playstation 3 Novo").constroi();
		leiloeiro.avalia(leilao);

	}

}
