package service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import dao.DrawHistoryDao;
import dao.RWSTarotDao;
import model.DrawHistory;
import model.RWSTarot;
import service.TarotService;
import service.session.DeckSession;

public class TarotServiceImpl implements TarotService {

	private final RWSTarotDao tarotDao;
	private final DrawHistoryDao historyDao;
	private final Random random;

	public TarotServiceImpl(RWSTarotDao tarotDao, DrawHistoryDao historyDao) {
		this(tarotDao, historyDao, new Random());
	}

	public TarotServiceImpl(RWSTarotDao tarotDao, DrawHistoryDao historyDao, Random random) {
		this.tarotDao = tarotDao;
		this.historyDao = historyDao;
		this.random = random;

	}

	@Override
	public DeckSession createSession() throws SQLException {
		return createSession(true);
	}

	@Override
	public DeckSession createSession(boolean enableReversed) throws SQLException {
		List<RWSTarot> all = tarotDao.findAll();
		if(all == null || all.isEmpty()) {
			throw new IllegalStateException("rws_tarot is empty");
		}
		
		List<RWSTarot> deck = new ArrayList<>(all);
		Collections.shuffle(deck, random);
		
		boolean[] reversedFlags = new boolean[deck.size()];
		if (enableReversed) {
			for (int i = 0; i < reversedFlags.length; i++) {
				reversedFlags[i] = random.nextBoolean();
			}
			
		}
		return new DeckSession(deck, reversedFlags);
	}

	@Override
	public DrawResult pickCards(DeckSession session, int position, Integer userId, String question)	throws SQLException {
		session.markPicked(position);
		
		RWSTarot card = session.getCardAt(position);
		boolean reversed = session.isReversedAt(position);
		String orientation = reversed ? "逆位" : "正位";
		String cardName = card.getCardNameZh();
		String q = question == null ? "" : question.trim();
		if (q.isEmpty()) {
			throw new IllegalArgumentException("question required");
		}
		
		String prompt = "你是一個專業的塔羅占卜師，請根據以下內容為我占卜：\n我的問題是：" + q + "\n抽到的牌是：" + cardName + "（" + orientation + "）\n請根據偉特塔羅的牌義進行解讀。";
		Integer historyId = null;
		if (userId != null) {
			DrawHistory h = new DrawHistory();
			h.setUserId(userId);
			h.setCardPk(card.getId());
			h.setQuestion(q);
			h.setReversed(reversed);
			
			int newId = historyDao.insert(h);
			historyId = (newId == 0) ? null : newId;
		}
		return new DrawResult(card, reversed, prompt, historyId);
	}

}
