package service;

import java.sql.SQLException;
import model.RWSTarot;
import service.session.DeckSession;

public interface TarotService {

	DeckSession createSession() throws SQLException;
	DeckSession createSession(boolean enableReversed) throws SQLException;
	
	DrawResult pickCards(DeckSession session, int position, Integer userId, String question)throws SQLException;
	
	class DrawResult {	
		private final RWSTarot card;
		private final boolean reversed;
		private final String prompt;
		private final Integer historyId;
		
		public DrawResult (RWSTarot card, boolean reversed, String prompt, Integer historyId) {
			this.card = card;
			this.reversed = reversed;
			this.prompt = prompt;
			this.historyId = historyId;
		}

		public RWSTarot getCard() {
			return card;
		}

		public boolean isReversed() {
			return reversed;
		}

		public String getPrompt() {
			return prompt;
		}

		public Integer getHistoryId() {
			return historyId;
		}
	}
	
}
