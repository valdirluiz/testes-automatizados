package br.com.caelum.leilao.servico;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.inOrder;

import br.com.caelum.leilao.builder.CriadorDeLeilao;
import br.com.caelum.leilao.dominio.Leilao;
import br.com.caelum.leilao.infra.dao.RepositorioDeLeiloes;

public class EncerradorDeLeilaoTest {

	private RepositorioDeLeiloes daoFalso;
	private EncerradorDeLeilao encerrador;
	private EnviadorDeEmail enviadorDeEmail;

	@Before
	public void setUp() {
		this.daoFalso = mock(RepositorioDeLeiloes.class);
		this.enviadorDeEmail = mock(EnviadorDeEmail.class);
		this.encerrador = new EncerradorDeLeilao(daoFalso, enviadorDeEmail);
	}

	@Test
	public void deveEncerrarLeiloesQueComecaramUmaSemanaAntes() {
		Calendar antiga = Calendar.getInstance();
		antiga.set(1999, 1, 20);

		Leilao leilao1 = new CriadorDeLeilao().para("TV de  Plasma")
				.naData(antiga).constroi();
		Leilao leilao2 = new CriadorDeLeilao().para("Geladeira").naData(antiga)
				.constroi();
		List<Leilao> leiloesAntigos = Arrays.asList(leilao1, leilao2);

		when(daoFalso.correntes()).thenReturn(leiloesAntigos);
		encerrador.encerra();

		assertTrue(leilao1.isEncerrado());
		assertTrue(leilao2.isEncerrado());
		assertEquals(2, encerrador.getTotalEncerrados());
	}

	@Test
	public void naoDeveEncerrarLeiloesQueComecaramMenosDeUmaSemanaAtras() {

		Calendar ontem = Calendar.getInstance();
		ontem.add(Calendar.DAY_OF_MONTH, -1);

		Leilao leilao1 = new CriadorDeLeilao().para("TV de plasma")
				.naData(ontem).constroi();
		Leilao leilao2 = new CriadorDeLeilao().para("Geladeira").naData(ontem)
				.constroi();

		when(daoFalso.correntes()).thenReturn(Arrays.asList(leilao1, leilao2));

		encerrador.encerra();

		assertEquals(0, encerrador.getTotalEncerrados());
		assertFalse(leilao1.isEncerrado());
		assertFalse(leilao2.isEncerrado());

		verify(daoFalso, never()).atualiza(leilao1);
		verify(daoFalso, never()).atualiza(leilao2);
	}

	@Test
	public void naoDeveEncerrarListaVazia() {
		when(daoFalso.correntes()).thenReturn(new ArrayList<Leilao>());

		encerrador.encerra();

		assertEquals(0, encerrador.getTotalEncerrados());
	}

	@Test
	public void deveAtualizarLeiloesEncerrados() {
		Calendar antiga = Calendar.getInstance();
		antiga.set(1999, 1, 20);

		Leilao leilao1 = new CriadorDeLeilao().para("TV de plasma")
				.naData(antiga).constroi();

		when(daoFalso.correntes()).thenReturn(Arrays.asList(leilao1));
		encerrador.encerra();

		InOrder inOrder = inOrder(daoFalso, enviadorDeEmail);
		inOrder.verify(daoFalso, times(1)).atualiza(leilao1);
		inOrder.verify(enviadorDeEmail, times(1)).envia(leilao1);
	}

	@Test
	public void deveEnviarEmailAposPersistirLeilaoEncerrado() {
		Calendar antiga = Calendar.getInstance();
		antiga.set(1999, 1, 20);

		Leilao leilao1 = new CriadorDeLeilao().para("TV de plasma").naData(antiga).constroi();

		when(daoFalso.correntes()).thenReturn(Arrays.asList(leilao1));

		encerrador.encerra();

		InOrder inOrder = inOrder(daoFalso, enviadorDeEmail);
		inOrder.verify(daoFalso, times(1)).atualiza(leilao1);
		inOrder.verify(enviadorDeEmail, times(1)).envia(leilao1);
	}

}
