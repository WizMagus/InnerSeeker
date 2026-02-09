package service.session;

import java.util.List;
import java.util.Objects;

import model.RWSTarot;

public class DeckSession {

	// 牌組生成用
	private final List<RWSTarot> deck;

	// 是否逆位，於牌組建立時決定
	private final boolean[] reversedFlags;

	// 檢查某位置是否被抽出
	private final boolean[] pickedFlags;

	public DeckSession(List<RWSTarot> shuffledDeck, boolean[] reversedFlags) {
		Objects.requireNonNull(shuffledDeck);
		Objects.requireNonNull(reversedFlags);

		// 牌組不得為空
		if (shuffledDeck.isEmpty()) {
			throw new IllegalArgumentException("shuffledDeck is empty");
		}
		// 逆位陣列需與牌組數量相同
		if (reversedFlags.length != shuffledDeck.size()) {
			throw new IllegalArgumentException("reversedFlags length mismatch");
		}

		this.deck = shuffledDeck;

		this.reversedFlags = reversedFlags;
		// 預設全部的牌都還沒被抽
		this.pickedFlags = new boolean[shuffledDeck.size()];
	}

	// 檢查抽牌位是否溢出邊界
	private void checkPosition(int position) {
		if (position < 0 || position >= deck.size()) {
			throw new IndexOutOfBoundsException("illegal position=" + position);
		}
	}

	public int size() {
		return deck.size();
	}

	public RWSTarot getCardAt(int position) {
		checkPosition(position);
		return deck.get(position);
	}

	public boolean isReversedAt(int position) {
		checkPosition(position);
		return reversedFlags[position];
	}

	public boolean isPicked(int position) {
		checkPosition(position);
		return pickedFlags[position];
	}

	public void markPicked(int position) {
		checkPosition(position);
		if (pickedFlags[position]) {
			throw new IllegalStateException("Position already picked: " + position);
		}
		pickedFlags[position] = true;
	}
}
