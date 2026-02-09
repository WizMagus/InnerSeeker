package service.impl;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import dao.DrawHistoryDao;
import dao.RWSTarotDao;
import model.DrawHistory;
import model.RWSTarot;
import model.Role;
import service.HistoryService;

public class HistoryServiceImpl implements HistoryService{

	
	private final DrawHistoryDao historyDao;
    private final RWSTarotDao tarotDao;

    public HistoryServiceImpl(DrawHistoryDao historyDao, RWSTarotDao tarotDao) {
        this.historyDao = historyDao;
        this.tarotDao = tarotDao;
    }
    
	@Override
	public List<DrawHistory> list(Integer userId, Role role, int limit) throws SQLException {
		int lim = (limit <= 0) ? 200 : limit;

        if (role == Role.ADMIN) return historyDao.findAll(lim);
        if (userId == null) return Collections.emptyList();
        return historyDao.findByUserId(userId, lim);
	}

	@Override
	public boolean delete(Integer userId, Role role, int historyId) throws SQLException {
		if (role == Role.ADMIN) return historyDao.deleteById(historyId);
        if (userId == null) return false;
        return historyDao.deleteByIdAndUserId(historyId, userId);
	}

	@Override
	public String buildPrompt(DrawHistory h) throws SQLException {
		String q = (h.getQuestion() == null) ? "" : h.getQuestion().trim();
        String orientation = h.isReversed() ? "逆位" : "正位";
        RWSTarot card = tarotDao.findById(h.getCardPk());
        String cardName = (card == null) ? String.valueOf(h.getCardPk()) : card.getCardNameZh();

        return "你是一個專業的塔羅占卜師，請根據以下內容為我占卜：\n"
             + "我的問題是：" + q + "\n"
             + "抽到的牌是：" + cardName + "（" + orientation + "）\n"
             + "請根據偉特塔羅的牌義進行解讀。";
	}

}
