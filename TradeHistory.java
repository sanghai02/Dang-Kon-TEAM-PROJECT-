package src;

import java.io.*;
import java.util.*;

public class TradeHistory {
    private int transactionId;
    private String sellerId;
    private String buyerId;
    private int itemNum;

    // 플래그: 신고 및 좋아요 상태
    private boolean sellerReport;
    private boolean sellerLike;
    private boolean buyerReport;
    private boolean buyerLike;

    public TradeHistory(int transactionId, String sellerId, String buyerId, int itemNum) {
        this.transactionId = transactionId;
        this.sellerId = sellerId;
        this.buyerId = buyerId;
        this.itemNum = itemNum;
        this.sellerReport = false;
        this.sellerLike   = false;
        this.buyerReport  = false;
        this.buyerLike    = false;
    }

    // 신고 여부 확인
    public boolean isAlreadyReportedBy(User user) {
        if (user.getId().equals(this.sellerId)) return sellerReport;
        if (user.getId().equals(this.buyerId))  return buyerReport;
        return false;
    }

    // 좋아요 여부 확인
    public boolean isAlreadyLikedBy(User user) {
        if (user.getId().equals(this.sellerId)) return sellerLike;
        if (user.getId().equals(this.buyerId))  return buyerLike;
        return false;
    }

    // 신고 처리 (reportScore 증가)
    public void report(User user, List<User> userList) {
        if (user.getId().equals(this.sellerId)) {
            this.sellerReport = true;
            User other = findUserById(this.buyerId, userList);
            if (other != null) other.incrementReportScore();
        } else if (user.getId().equals(this.buyerId)) {
            this.buyerReport = true;
            User other = findUserById(this.sellerId, userList);
            if (other != null) other.incrementReportScore();
        }
    }

    // 좋아요 처리 (reportScore 감소, 0 미만 방지)
    public void like(User user, List<User> userList) {
        if (user.getId().equals(this.sellerId)) {
            this.sellerLike = true;
            User other = findUserById(this.buyerId, userList);
            if (other != null) other.decrementReportScore();
        } else if (user.getId().equals(this.buyerId)) {
            this.buyerLike = true;
            User other = findUserById(this.sellerId, userList);
            if (other != null) other.decrementReportScore();
        }
    }

    private static User findUserById(String id, List<User> userList) {
        for (User u : userList) {
            if (u.getId().equals(id)) return u;
        }
        return null;
    }

    // 거래 파일 로드
    public static List<TradeHistory> loadTrades(String filename) {
        List<TradeHistory> trades = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if (p.length < 8) continue;
                int tid       = Integer.parseInt(p[0].trim());
                String seller = p[1].trim();
                int item      = Integer.parseInt(p[2].trim());
                int sReport   = Integer.parseInt(p[3].trim());
                int sLike     = Integer.parseInt(p[4].trim());
                String buyer  = p[5].trim();
                int bReport   = Integer.parseInt(p[6].trim());
                int bLike     = Integer.parseInt(p[7].trim());

                TradeHistory th = new TradeHistory(tid, seller, buyer, item);
                th.sellerReport = (sReport == 1);
                th.sellerLike   = (sLike   == 1);
                th.buyerReport  = (bReport == 1);
                th.buyerLike    = (bLike   == 1);
                trades.add(th);
            }
        } catch (IOException e) {
            System.out.println("거래 내역 로드 실패: " + e.getMessage());
        }
        return trades;
    }

    // 거래 파일 저장
    public static void saveTrades(String filename, List<TradeHistory> list) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
            for (TradeHistory th : list) {
                pw.printf("%d,%s,%d,%d,%d,%s,%d,%d%n",
                        th.transactionId,
                        th.sellerId,
                        th.itemNum,
                        th.sellerReport ? 1 : 0,
                        th.sellerLike   ? 1 : 0,
                        th.buyerId,
                        th.buyerReport  ? 1 : 0,
                        th.buyerLike    ? 1 : 0
                );
            }
        } catch (IOException e) {
            System.out.println("거래 내역 저장 실패: " + e.getMessage());
        }
    }

    // 설계문서와 동일한 이름의 메서드로 통합
    public static void processReportOrLike(
            String input,
            User currentUser,
            List<TradeHistory> tradeList,
            List<User> userList
    ) {
        // 정확히 'S-숫자' 또는 'L-숫자' 형식만 허용 (leading zeros 금지)
        if (!input.matches("[SL]-[1-9]\\d*")) {
            System.out.println("잘못된 입력입니다. 로그인 후 메뉴로 이동합니다.");
            return;
        }
        String[] parts = input.split("-");
        String action = parts[0];
        int idx = Integer.parseInt(parts[1]);

        List<TradeHistory> myTrades = getTradesByUser(tradeList, currentUser);
        if (myTrades.isEmpty()) {
            System.out.println("등록된 거래 내역이 없습니다.");
            return;
        }
        if (idx < 1 || idx > myTrades.size()) {
            System.out.println("잘못된 입력입니다. 로그인 후 메뉴로 이동합니다.");
            return;
        }
        TradeHistory th = myTrades.get(idx - 1);

        if ("S".equals(action)) {
            if (th.isAlreadyReportedBy(currentUser)) {
                System.out.println("이미 신고된 거래입니다.");
                return;
            } else if (th.isAlreadyLikedBy(currentUser)) {
                System.out.println("이미 좋아요가 등록된 거래입니다.");
                return;
            }
            th.report(currentUser, userList);
            saveTrades("TransactionFile.txt", tradeList);
            System.out.println("신고가 접수 되었습니다.");
            return;
        } else if ("L".equals(action)) {
            if (th.isAlreadyLikedBy(currentUser)) {
                System.out.println("이미 좋아요가 등록된 거래입니다.");
                return;
            } else if (th.isAlreadyReportedBy(currentUser)) {
                System.out.println("이미 신고된 거래입니다.");
                return;
            }
            th.like(currentUser, userList);
            saveTrades("TransactionFile.txt", tradeList);
            System.out.println("좋아요가 등록 되었습니다.");
            return;
        } else {
            System.out.println("잘못된 입력입니다. 로그인 후 메뉴로 이동합니다.");
            return;
        }
    }

    // 사용자별 거래 필터
    public static List<TradeHistory> getTradesByUser(List<TradeHistory> tradeList, User user) {
        List<TradeHistory> result = new ArrayList<>();
        for (TradeHistory th : tradeList) {
            if (th.sellerId.equals(user.getId()) || th.buyerId.equals(user.getId())) {
                result.add(th);
            }
        }
        return result;
    }

    // 설계문서에 따른 Getter 추가
    /** 판매자 ID 반환 */
    public String getSellerId() {
        return this.sellerId;
    }

    /** 구매자 ID 반환 */
    public String getBuyerId() {
        return this.buyerId;
    }

    /** 거래된 아이템 번호 반환 */
    public int getItemNum() {
        return this.itemNum;
    }
}
