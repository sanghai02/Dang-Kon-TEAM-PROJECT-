package src;

import java.util.ArrayList;
import java.util.List;

public class User {
    // 기본 사용자 정보
    public String ID;
    public String password;

    // 신뢰도 평가
    public double trustEvaluation;
    public int evaluationCount;

    // 뱃지 및 등급
    public String badge;          // e.g. "BRONZE", "SILVER", "GOLD"
    public String memberGrade;    // e.g. "VVIP", "VIP", "COMMON", "DANGER"

    // 신고 점수
    public int reportScore;

    // 거래 내역(상품 번호) 리스트
    public List<Integer> sell;
    public List<Integer> buy;
    public List<Integer> dib;

    // 상품 등록 권한 플래그
    private boolean canRegister;

    // 회원 등급용 열거형
    public enum Grade { VVIP, VIP, COMMON, DANGER }

    /**
     * TransactionFile.txt 로드 시 사용하는 생성자
     */
    public User(String ID, String password,
                double trustEvaluation, int evaluationCount,
                String badge, String memberGrade, int reportScore) {
        this.ID               = ID;
        this.password         = password;
        this.trustEvaluation  = trustEvaluation;
        this.evaluationCount  = evaluationCount;
        this.badge            = badge;
        this.memberGrade      = memberGrade;
        this.reportScore      = reportScore;
        this.sell             = new ArrayList<>();
        this.buy              = new ArrayList<>();
        this.dib              = new ArrayList<>();
        this.canRegister      = true;  // 기본적으로 등록 허용
    }

    /**
     * 회원가입 시 사용하는 생성자
     */
    public User(String ID, String password) {
        this.ID               = ID;
        this.password         = password;
        this.trustEvaluation  = 0.0;
        this.evaluationCount  = 0;
        this.badge            = "BRONZE";
        this.memberGrade      = "";
        this.reportScore      = 0;
        this.sell             = new ArrayList<>();
        this.buy              = new ArrayList<>();
        this.dib              = new ArrayList<>();
        this.canRegister      = true;  // 기본적으로 등록 허용
    }

    /**
     * 신고/좋아요 합계와 신뢰도를 바탕으로 등급 계산
     */
    public Grade computeGrade() {
        int feedbackSum = reportScore;  // reportScore에 신고 누적–좋아요 누적을 반영해둔 값
        // VVIP: GOLD 배지, 피드백 ≤5, 신뢰도 ≥4.5
        if ("GOLD".equals(badge) && feedbackSum <= 5 && trustEvaluation >= 4.5) {
            return Grade.VVIP;
        }
        // VIP: GOLD 배지, 1 ≤ 피드백 <5, 신뢰도 ≥4.5
        if ("GOLD".equals(badge) && feedbackSum >= 1 && feedbackSum < 5 && trustEvaluation >= 4.5) {
            return Grade.VIP;
        }
        // COMMON: SILVER 배지 이상, 5 ≤ 피드백 <10, 신뢰도 ≥3.0
        if (("SILVER".equals(badge) || "GOLD".equals(badge))
                && feedbackSum >= 5 && feedbackSum < 10 && trustEvaluation >= 3.0) {
            return Grade.COMMON;
        }
        // 나머지 DANGER
        return Grade.DANGER;
    }

    /**
     * computeGrade() 결과를 memberGrade, canRegister에 반영
     */
    public void updateGradeAndPermission() {
        Grade newGrade = computeGrade();
        this.memberGrade = newGrade.name();
        this.canRegister  = (newGrade != Grade.DANGER);
    }

    /**
     * 상품 등록 권한 조회
     */
    public boolean canRegister() {
        return canRegister;
    }

    // ----------------------------------------
    // 추가된 메서드: TradeHistory 연동용
    // ----------------------------------------

    /**
     * 사용자 ID 반환
     */
    public String getId() {
        return this.ID;
    }

    /**
     * reportScore 1 증가
     */
    public void incrementReportScore() {
        this.reportScore++;
    }

    /**
     * reportScore 1 감소 (최저 0 유지)
     */
    public void decrementReportScore() {
        if (this.reportScore > 0) {
            this.reportScore--;
        }
    }
}