package src;

import src.User;

import java.io.*;
import java.util.*;
import java.math.BigInteger;
import java.util.stream.Collectors;
import java.util.List;

import static java.lang.System.exit;

public class DangKon {
    ArrayList<User> userList = new ArrayList<>();
    ArrayList<Item> itemList = new ArrayList<>();
    //ArrayList<String> categoryList = new ArrayList<>();
    ArrayList<TradeHistory> tradeList = new ArrayList<>();
    int productNumMax;
    static Scanner scan = new Scanner(System.in);
    User currentUserInfo;
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    //run() ----------------------------------------------------------
    // 파일 로드 및 배열 저장
    public void run(String filename1, String filename2, String filename3) {
        File userFile = new File(filename1);
        try {
            if (userFile.createNewFile()) {
                System.out.println("파일 생성됨: " + userFile.getAbsolutePath());
            } else {
                try {
                    Scanner scanner = new Scanner(new File(filename1));
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        String[] comma = line.split(",", -1); //8개 나옴
                        String ID;
                        String password;
                        float trustEvaluation;
                        int evaluationCount;
                        String badge;
                        String memberGrade;
                        int reportScore;
                        try {
                            ID = comma[0].trim();
                            password = comma[1].trim();
                            trustEvaluation = Float.parseFloat(comma[2].trim());
                            evaluationCount = Integer.parseInt(comma[3].trim());
                            badge = comma[4].trim();
                            memberGrade = comma[5].trim();
                            reportScore = Integer.parseInt(comma[6].trim());
                        } catch (NoSuchElementException e) {
                            notCorrectFileGrammar();
                            return;
                        }
                        User user = new User(ID, password, trustEvaluation, evaluationCount, badge, memberGrade, reportScore);
                        user.sell = new ArrayList<>();
                        user.buy = new ArrayList<>();
                        user.dib = new ArrayList<>();

                        String[] semicolon = comma[7].split("\t;");
                        if (semicolon.length == 3) {
                            StringTokenizer st1 = new StringTokenizer(semicolon[0], "\t");
                            StringTokenizer st2 = new StringTokenizer(semicolon[1], "\t");
                            StringTokenizer st3 = new StringTokenizer(semicolon[2], "\t");

                            if (st1.hasMoreTokens()) {
                                while (st1.hasMoreTokens()) {
                                    int itemNum = Integer.parseInt(st1.nextToken());
                                    user.sell.add(itemNum);
                                }
                            }

                            if (st2.hasMoreTokens()) {
                                while (st2.hasMoreTokens()) {
                                    int itemNum = Integer.parseInt(st2.nextToken());
                                    user.buy.add(itemNum);
                                }
                            }

                            if (st3.hasMoreTokens()) {
                                while (st3.hasMoreTokens()) {
                                    int itemNum = Integer.parseInt(st3.nextToken());
                                    user.dib.add(itemNum);
                                }
                            }

                        } else {
                            notCorrectFileGrammar();
                            break;
                        }
                        userList.add(user);
                    }

                    for (User user : userList) { //파일 잘 로드되는지 확인차 넣었어요!! 배포 전에 삭제해요
                        System.out.println(user);
                    }


                } catch (FileNotFoundException e) {
                    System.out.println("파일을 찾을 수 없습니다.");
                    exit(1);
                }
            }
        } catch (IOException e) {
            System.out.println("파일 생성 실패");
        }

        File productFile = new File(filename2);
        try {
            if (productFile.createNewFile()) {
                System.out.println("파일 생성됨: " + productFile.getAbsolutePath());
            } else {
                try {
                    Scanner scanner = new Scanner(new File(filename2));
                    while (scanner.hasNextLine()) {
                        String line = scanner.nextLine();
                        String[] comma = line.split(","); //6개 나옴
                        int itemNum;
                        String name;
                        String price;
                        String category;
                        String dibId;
                        int status;
                        try {
                            itemNum = Integer.parseInt(comma[0].trim());
                            name = comma[1].trim();
                            price = comma[2].trim();
                            category = comma[3].trim();
                            dibId = comma[4].trim();
                            status = Integer.parseInt(comma[5].trim());
                            Item item = new Item(itemNum, name, price, category, dibId, status);
                            itemList.add(item);
                        } catch (NoSuchElementException e) {
                            notCorrectFileGrammar();
                            return;
                        }
                        productNumMax = itemNum;
                    }

                    for (Item item : itemList) { //파일 잘 로드되는지 확인차 넣었어요!! 배포 전에 삭제해요
                        System.out.println(item);
                    }


                } catch (FileNotFoundException e) {
                    System.out.println("파일을 찾을 수 없습니다.");
                    exit(1);
                }
            }
        } catch (IOException e) {
            System.out.println("파일 생성 실패");
        }
        File transactionFile = new File(filename3);
        try {
            if (transactionFile.createNewFile()) {
                System.out.println("파일 생성됨: " + transactionFile.getAbsolutePath());
            } else {
                tradeList = new ArrayList<>(TradeHistory.loadTrades(filename3));
            }
        } catch (IOException e) {
            System.out.println("Transaction 파일 생성/로드 실패: " + e.getMessage());
        }

        beforeLogin();
    }

    //notCorrectFileGrammar() -----------------------------------------------------
    // 파일이 문법 형식에 맞지 않을 경우 return시키고 main함수 종료
    private void notCorrectFileGrammar() {
        System.out.println("파일이 문법 형식에 맞게 저장되어 있지 않습니다. 파일을 확인해주세요.");
    }

    //beforeLogin() ---------------------------------------------------------------
    //로그인 전 메인 메뉴 1 - 3까지의 옵션 선택
    private void beforeLogin() {
        System.out.println("----------------------------------------");
        while (true) {
            System.out.println("중고거래 당건에 오신 것을 환영합니다");
            System.out.println("1. 로그인 / 2. 회원가입 / 3. 종료");
            System.out.println("이용할 메뉴의 번호를 입력하세요");
            System.out.println("----------------------------------------");
            System.out.print("-->>> ");
            String input = scan.nextLine();
            if(input.equals("1")){
                login();
            }else if(input.equals("2")){
                join();
            }else if(input.equals("3")){
                //fileSaver();
                System.out.println("프로그램이 종료되었습니다.");
                exit(0);
            }else{
                System.out.println("메뉴의 번호가 올바르지 않습니다. 다시 입력해주세요.");
            }
        }
    }

    private boolean isValidID(String id) {
        return id.matches("^(?=.*[a-z])(?=.*\\d)[a-z\\d]{1,12}$");
    }

    private boolean isValidPassword(String pw) {
        return pw.matches("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[^a-zA-Z\\d])[\\da-zA-Z!@#$%^&*]{1,15}$");
    }

    private boolean isDuplicateID(String id) {
        for (User user : userList) {
            if (user.ID.equals(id)) {
                return true;
            }
        }
        return false;
    }

    private void join() {
        System.out.println("아이디를 입력하세요 (영문숫자포함 12자 이내, 공백X)");
        System.out.print("-->>> ");
        String id = scan.nextLine();

        if (id.contains(" ") || id.contains("\t")) {
            System.out.println("문법형식에 맞게 다시 입력하세요.");
            System.out.println("----------------------------------------");
            return;
        }

        if (!isValidID(id)) {
            System.out.println("문법형식에 맞게 다시 입력하세요.");
            System.out.println("----------------------------------------");
            return;
        }
        if (isDuplicateID(id)) {
            System.out.println("중복된 아이디 입니다. 다시 입력하세요.");
            System.out.println("----------------------------------------");
            return;
        }

        System.out.println("사용가능한 아이디입니다.");

        System.out.println("비밀번호를 입력하세요 특수문자영문숫자포함 15자 이내(공백X)");
        System.out.print("-->>> ");
        String pw = scan.nextLine();

        if (pw.contains(" ") || pw.contains("\t")) {
            System.out.println("문법형식에 맞게 다시 입력하세요.");
            System.out.println("----------------------------------------");
            return;
        }

        if (!isValidPassword(pw)) {
            System.out.println("문법형식에 맞게 다시 입력하세요.");
            System.out.println("----------------------------------------");
            return;
        }

        User newUser = new User(id, pw);
        userList.add(newUser);
        fileSaver();
        System.out.println(userList.size());
    }

    private void login() {
        System.out.println("아이디와 비밀번호를 띄어쓰기로(공백 문자 하나 이상) 구분하여 입력해주세요");
        System.out.print("-->>> ");
        String line = scan.nextLine();

        if (line.startsWith(" ") || line.endsWith(" ") || line.contains("\t")) {
            System.out.println("입력 형식이 잘못되었습니다.");
            System.out.println("----------------------------------------");
            return;
        }

        String[] input = line.split(" +");
        if (input.length != 2) {
            System.out.println("입력 형식이 잘못되었습니다.");
            System.out.println("----------------------------------------");
            return;
        }

        String id = input[0];
        String pw = input[1];

        for (User user : userList) {
            if (user.ID.equals(id)) {
                if (user.password.equals(pw)) {
                    System.out.println("로그인 되셨습니다");
                    System.out.println("----------------------------------------");
                    currentUserInfo = user;
                    currentUserInfo.sell = user.sell;
                    currentUserInfo.buy = user.buy;
                    afterLogin(user);
                } else {
                    System.out.println("잘못된 비밀번호를 입력하셨습니다.");
                    System.out.println("----------------------------------------");
                }
                return;
            }
        }
        System.out.println("올바르지 않은 아이디 입니다.");
        System.out.println("----------------------------------------");
    }

    //afterlogin() -----------------------------------------------------------------
    //login() 함수를 통과하고 실행되는 매소드 1 - 5까지의 옵션 선택
    private void afterLogin(User user) { //로그인 후 메뉴
        while (true) {
            System.out.println(user.ID + "님 환영합니다");
            System.out.println("1. 구매 / 2. 등록 / 3. 거래내역 / 4. 로그아웃 / 5. 회원 등급 / 6. 종료");
            System.out.println("이용할 메뉴의 번호를 입력하세요");
            System.out.println("----------------------------------------");
            System.out.print("-->>> ");

            try {
                String input = scan.nextLine();
                switch (input) {
                    case "1" -> search(user);
                    case "2" -> registerMenu(currentUserInfo);
                    case "3" -> tradeHistory(currentUserInfo);
                    case "4" -> {
                        System.out.println("로그아웃 되었습니다.");
                        System.out.println("----------------------------------------");
                        beforeLogin();
                    }
                    case "5" -> gradeMenu(currentUserInfo);
                    case "6" -> {
                        System.out.println("프로그램이 종료되었습니다.");
                        exit(0);
                    }
                    default -> System.out.println("메뉴의 번호가 올바르지 않습니다. 다시 입력해주세요.");
                }
            } catch (InputMismatchException e) {
                System.out.println("메뉴의 번호가 올바르지 않습니다. 다시 입력해주세요.");
                scan.nextLine();
            } catch (NumberFormatException e) {
                System.out.println("메뉴의 번호가 올바르지 않습니다. 다시 입력해주세요.");
            }
        }
    }


    private void fileSaver() {
        try (PrintWriter userWriter = new PrintWriter(new FileWriter("UserFile.txt"))) {
            for (User user : userList) {

                StringBuilder sb = new StringBuilder();

                sb.append(user.ID).append(",");
                sb.append(user.password).append(",");
                sb.append(user.trustEvaluation).append(",");
                sb.append(user.evaluationCount).append(",");
                sb.append(user.badge).append(",");
                sb.append(user.memberGrade != null ? user.memberGrade : "").append(",");
                sb.append(user.reportScore).append(",");

                sb.append("\t");
                if (user.sell != null && !user.sell.isEmpty()) {
                    for (int num : user.sell) {
                        sb.append(num).append("\t");
                    }
                }
                sb.append(";\t");

                if (user.buy != null && !user.buy.isEmpty()) {
                    for (int num : user.buy) {
                        sb.append(num).append("\t");
                    }
                }
                sb.append(";\t");

                if (user.dib != null && !user.dib.isEmpty()) {
                    for (int num : user.dib) {
                        sb.append(num).append("\t");
                    }
                }
                //sb.append(";"); 혹시몰라서 지우진않았습니다.
                userWriter.println(sb);
            }
        } catch (IOException e) {
            System.out.println("UserFile 저장 중 오류 발생: " + e.getMessage());
        }

        try (PrintWriter productWriter = new PrintWriter(new FileWriter("ProductFile.txt"))) {
            for (Item item : itemList) {
                productWriter.println(
                        item.itemNum + "," +
                                (item.name) + "," +
                                (item.price) + "," +
                                (item.category != null ? item.category : "") + "," +
                                (item.dibID != null ? item.dibID : "") + "," +
                                item.status
                );
            }
        } catch (IOException e) {
            System.out.println("ProductFile 저장 중 오류 발생: " + e.getMessage());
        }
    }


    private boolean isValidName(String name) {// 상품검색할때 이름-숫자-숫자에서 이름이 형식에 맞는지
        return name.matches("[가-힣a-zA-Z0-9 ]+"); //한글,영어,숫자,공백만!!!!!!!!!!!!!!!(+는 하나이상 반복하라는뜻)
    }

    // 숫자만 있는지 확인
    private boolean isNumeric(String str) {// 상품검색할때 이름-숫자-숫자에서 숫자가 형식에 맞는지
        return str.matches("\\d+");// 숫자만!!!!!!!!
    }

    // 잘못된 형식일 때
    private void invalidFormat(User login) { //형식 안맞으면 메뉴 돌아가기
        System.out.println("상품명-숫자-숫자 또는 상품명-숫자 또는 상품명 형식으로 입력해주세요.");
        System.out.println("----------------------------------------");
        afterLogin(login); // 다시 메뉴로 돌아가기
    }

    private void search(User Login) {
        System.out.println("1. 일반 검색 / 2. 카테고리 검색 / 0. 뒤로가기");
        System.out.println("이용할 검색 방법의 번호를 입력하세요");
        System.out.println("----------------------------------------");
        System.out.print("-->>> ");


        try {
            String input = scan.nextLine();
            switch (input) {
                case "1" -> {
                    generalSearch(Login);
                }
                case "2" -> {
                    searchByCategory("ProductFile.txt");
                }
                case "0" -> {
                    afterLogin(Login);
                }
                default -> {
                    System.out.println("메뉴의 번호가 올바르지 않습니다. 로그인 후 메뉴로 이동합니다.");
                    afterLogin(Login);
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("메뉴의 번호가 올바르지 않습니다. 로그인 후 메뉴로 이동합니다.");
            afterLogin(Login);
        } catch (NumberFormatException e) {
            System.out.println("메뉴의 번호가 올바르지 않습니다. 로그인 후 메뉴로 이동합니다.");
            afterLogin(Login);
        }
    }

    private void generalSearch(User login) {
        System.out.println("구매하고 싶은 상품을 검색하세요");
        System.out.println("----------------------------------------");
        System.out.println("상품명-가격-가격 -> 두 가격 사이의 상품 검색");
        System.out.println("상품명-가격     -> 해당 가격 이하의 상품 검색");
        System.out.println("상품명         -> 해당 상품 모두 검색");
        System.out.println("각 요소 사이는 '-'로 구분해주세요");
        System.out.println("----------------------------------------");
        System.out.print("-->>> ");

        String search = scan.nextLine();
        String[] parts = search.split("-", -1);
        ArrayList<Item> ItemList = new ArrayList<>();//회원에게 보여줄 판매가능 상품 리스트

        if (parts.length == 1) {
            if (isValidName(parts[0])) {
                String keyword = parts[0];
                for (User u : userList) {
                    for (int i : u.sell) {
                        int itmNum = i;
                        for (Item itm : itemList) {
                            if (itm.itemNum == itmNum) {
                                if (itm.status == 0 && itm.name.contains(keyword)) {
                                    ItemList.add(itm);
                                    break;
                                    //UserList.add(u);
                                }
                            }
                        }
                    }
                }
            } else {
                invalidFormat(login);
            }
        } else if (parts.length == 2) {
            if (isValidName(parts[0]) && isNumeric(parts[1])) {
                String keyword = parts[0];
                BigInteger maxPrice = new BigInteger(parts[1]);
                for (User u : userList) {
                    for (int i : u.sell) {
                        int itmNum = i;
                        for (Item itm : itemList) {
                            if (itm.itemNum == itmNum) {
                                BigInteger comparePrice = new BigInteger(itm.price);
                                if (itm.status == 0 && itm.name.contains(keyword) && comparePrice.compareTo(maxPrice) <= 0) {
                                    ItemList.add(itm);
                                    break;
                                    //UserList.add(u);
                                }
                            }
                        }
                    }
                }
            } else {
                invalidFormat(login);
            }
        } else if (parts.length == 3) {
            if (isValidName(parts[0]) && isNumeric(parts[1]) && isNumeric(parts[2])) {
                String keyword = parts[0];
                BigInteger price1 = new BigInteger(parts[1]);
                BigInteger price2 = new BigInteger(parts[2]);
                BigInteger lower;
                BigInteger upper;
                if (price1.compareTo(price2) <= 0) {
                    lower = new BigInteger(parts[1]);
                    upper = new BigInteger(parts[2]);
                } else {
                    lower = new BigInteger(parts[2]);
                    upper = new BigInteger(parts[1]);
                }
                for (User u : userList) {
                    for (int i : u.sell) {
                        int itmNum = i;
                        for (Item itm : itemList) {
                            if (itm.itemNum == itmNum) {
                                BigInteger comparePrice = new BigInteger(itm.price);
                                if (itm.status == 0 && itm.name.contains(keyword) && comparePrice.compareTo(lower) >= 0
                                        && comparePrice.compareTo(upper) <= 0) {
                                    ItemList.add(itm);
                                    break;
                                    //UserList.add(u);
                                }
                            }
                        }
                    }
                }
            } else {
                invalidFormat(login);
            }
        } else {
            invalidFormat(login);
        }

        if (ItemList.isEmpty()) {
            System.out.println("조건에 부합하는 상품이 없습니다.");
            System.out.println("----------------------------------------");
            return;
        }

        System.out.println("----------------------------------------");
        for (int i = 0; i < ItemList.size(); i++) {
            Item item = ItemList.get(i);
            //User user = UserList.get(i);
            String seller = "";
            for (User u : userList) {
                for (int num : u.sell) {
                    if (item.itemNum == num) {
                        seller = u.ID;
                    }
                }
            }
            System.out.println((i + 1) + "번 상품 : " + item.name + "\n가격 : " + item.price + "\n판매자 : " + seller + "\n----------------------------------------");
        }
        dibMenu(currentUserInfo, ItemList);
    }

    public static String[] extractCategoriesFromProducts(String filePath) {
        Set<String> categorySet = new LinkedHashSet<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length >= 4) {
                    String category = fields[3].trim();
                    if (!category.isEmpty() && !category.equals("0")) {
                        categorySet.add(category);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("상품 파일을 읽는 중 오류가 발생했습니다.");
            return new String[0]; // 오류 발생 시 빈 배열 반환
        }

        return categorySet.toArray(new String[0]);
    }

    private void searchByCategory(String productFilePath) {
        String[] categoryList = extractCategoriesFromProducts(productFilePath);

        if (categoryList.length == 0) {
            System.out.println("유효한 카테고리가 존재하지 않습니다.");
            return;
        }

        System.out.println("검색하고 싶은 카테고리의 인덱스를 입력하세요. 뒤로가기는 '0'");
        System.out.println("----------------------------------------");

        for (int i = 0; i < categoryList.length; i++) {
            System.out.println((i + 1) + ". " + categoryList[i]); // 1번부터 시작
        }

        System.out.println("----------------------------------------");
        System.out.print("-->>> ");
        String input = scan.nextLine();
        int index;

        try {
            index = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("숫자만 입력해주세요.");
            return;
        }

        if (index == 0) {
            return; // 상위 메뉴 복귀
        }

        if (index < 1 || index > categoryList.length) {
            System.out.println("해당 번호의 카테고리는 존재하지 않습니다.");
            return;
        }

        String selectedCategory = categoryList[index - 1];
        boolean found = false;
        List<String[]> filteredProducts = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(productFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length >= 6) {
                    String category = fields[3].trim();
                    if (category.equals(selectedCategory) && fields[5].trim().equals("0")) {
                        filteredProducts.add(fields);
                        found = true;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("상품 파일을 읽는 중 오류가 발생했습니다.");
            return;
        }

        if (!found) {
            System.out.println("해당 카테고리에 상품이 없습니다.");
            return;
        }

        System.out.println("----------------------------------------");
        for (int i = 0; i < filteredProducts.size(); i++) {
            String[] p = filteredProducts.get(i);
            System.out.println((i + 1) + "번 상품 : " + p[1]);
            System.out.println("가격 : " + p[2]);
            System.out.println("판매자 : " + (p[4].isEmpty() ? "미등록" : p[4]));
            System.out.println("----------------------------------------");
        }
        ArrayList<Item> filteredItems = new ArrayList<>();

        for (String[] product : filteredProducts) {
            int itemNum = Integer.parseInt(product[0].trim());
            String name = product[1].trim();
            String price = product[2].trim();
            String category = product[3].trim();
            String dibID = product[4].trim();
            int status = Integer.parseInt(product[5].trim());

            Item item = new Item(itemNum, name, price, category, dibID, status);
            filteredItems.add(item);
        }

        dibMenu(currentUserInfo, filteredItems);

    }

    private ArrayList<Item> convertToItemList(List<String[]> rawProducts) {
        ArrayList<Item> itemList = new ArrayList<>();
        for (String[] p : rawProducts) {
            int itemNum = Integer.parseInt(p[0]);
            String name = p[1];
            String price = p[2]; // String 유지
            String category = p[3];
            String dibID = p[4]; // 필드명에 맞춤
            int status = Integer.parseInt(p[5]);

            itemList.add(new Item(itemNum, name, price, category, dibID, status));
        }
        return itemList;
    }

    public void dibMenu(User login, ArrayList<Item> ItemList) {
        while (true) {
            System.out.println("1. 찜 / 2. 찜 확인 및 취소 / 3. 구매 / 0. 뒤로가기");
            System.out.print("-->>> ");
            String input = scan.nextLine();

            if (!(input.equals("0") || input.equals("1") || input.equals("2") || input.equals("3"))) {
                System.out.println("메뉴의 번호가 올바르지 않습니다. 다시 입력해주세요.");
                continue;
            }

            int select = Integer.parseInt(input);

            switch (select) {
                case 1 -> {
                    callDibs(ItemList, login);
                    break;
                }
                case 2 -> {
                    checkAndCancelDibs(login, itemList, userList);
                    break;
                }
                case 3 -> {
                    Purchase(ItemList, login);
                    break;
                }
                case 0 -> {
                    return;
                }
            }
            break;
        }
    }
    public void callDibs(ArrayList<Item> searchingItem, User currentUserInfo) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("찜할 상품의 인덱스를 입력하세요. 뒤로가기 '0'\n-->>> ");
            String input = scanner.nextLine();

            // 뒤로가기
            if (input.equals("0")) return;

            // 입력값이 정확한 10진수 정수인지 확인 (공백, 음수, 01, 소수 등 모두 차단)
            if (!input.matches("^[1-9][0-9]*$")) {
                System.out.println("상품 리스트에 존재하는 인덱스 숫자를 입력해주세요.");
                continue;
            }

            int index;
            try {
                index = Integer.parseInt(input) - 1;
            } catch (NumberFormatException e) {
                System.out.println("상품 리스트에 존재하는 인덱스 숫자를 입력해주세요.");
                continue;
            }

            // 범위 초과
            if (index < 0 || index >= searchingItem.size()) {
                System.out.println("상품 리스트에 존재하는 인덱스 숫자를 입력해주세요.");
                continue;
            }

            Item selectedItem = searchingItem.get(index);

            // itemList에서 원본 객체 찾기
            Item realItem = null;
            for (Item i : itemList) {
                if (i.itemNum == selectedItem.itemNum) {
                    realItem = i;
                    break;
                }
            }

            if (realItem == null) {
                System.out.println("상품을 찾을 수 없습니다.");
                return;
            }

            // 이미 다른 사용자가 찜한 상품
            if (realItem.dibID != null && !realItem.dibID.isEmpty()) {
                System.out.println("이미 다른 고객님이 찜한 상품입니다. 다른 상품을 선택해주세요");
                continue;
            }

            // 찜 처리
            realItem.dibID = currentUserInfo.ID;
            currentUserInfo.dib.add(realItem.itemNum);

            System.out.println("찜 완료!");

            fileSaver();
            afterLogin(currentUserInfo);
            break;
        }
    }
    public void checkAndCancelDibs(User currentUserInfo, ArrayList<Item> itemList, ArrayList<User> userList) {
        while (true) {
            ArrayList<Item> dibItems = new ArrayList<>();
            System.out.println("----------------------------------------");

            // 찜한 아이템 찾기
            for (int itemNum : currentUserInfo.dib) {
                Item item = findItemByNum(itemNum);
                if (item != null) {
                    String seller = userList.stream()
                            .filter(u -> u.sell != null && u.sell.contains(itemNum))
                            .map(u -> u.ID)
                            .findFirst()
                            .orElse("Unknown");

                    System.out.printf("%d번 상품 : %s\n가격 : %s\n판매자 : %s\n", dibItems.size() + 1, item.name, item.price, seller);
                    System.out.println("----------------------------------------");
                    dibItems.add(item);
                }
            }

            if (dibItems.isEmpty()) {
                System.out.println("찜한 상품이 없습니다.");
                return;
            }

            // 메뉴 출력
            System.out.println("1. 구매 / 2. 찜 취소 / 0. 뒤로가기");
            System.out.print("-->>> ");
            String input = scan.nextLine().trim();

            switch (input) {
                case "1" -> {
                    int idx = getItemIndexFromUser(dibItems.size());
                    if (idx == -1) return;
                    Item toBuy = dibItems.get(idx);
                    Purchase(new ArrayList<>(List.of(toBuy)), currentUserInfo);
                    fileSaver();
                    return;
                }
                case "2" -> {
                    int idx = getItemIndexFromUser(dibItems.size());
                    if (idx == -1) continue;
                    Item toCancel = dibItems.get(idx);
                    currentUserInfo.dib.remove((Integer) toCancel.itemNum);
                    if (toCancel.dibID != null && toCancel.dibID.equals(currentUserInfo.ID)) {
                        toCancel.dibID = "";
                    }
                    fileSaver();
                }
                case "0" -> {
                    return;
                }
                default -> {
                    System.out.println("올바른 메뉴 번호를 입력해주세요.");
                }
            }
        }
    }

    // 사용자에게 인덱스 입력 받는 헬퍼
    private int getItemIndexFromUser(int maxSize) {
        System.out.print("상품 인덱스를 입력하세요. 0은 취소 >> ");
        String input = scan.nextLine().trim();
        if (input.equals("0")) return -1;
        try {
            int idx = Integer.parseInt(input);
            if (idx >= 1 && idx <= maxSize) return idx - 1;
        } catch (NumberFormatException ignored) {
        }
        System.out.println("잘못된 입력입니다.");
        return -1;
    }

    // DangKon 클래스 내부에 추가
    private String listToTabString(List<Integer> list) {
        if (list == null || list.isEmpty()) return "";
        return list.stream().map(String::valueOf).collect(Collectors.joining("\t"));
    }

    private void Purchase(ArrayList<Item> itemList, User currentUserInfo) {
        System.out.println("구매할 상품의 인덱스 번호를 입력하세요. 뒤로가기는 '0'");

        while (true) {
            System.out.print("-->>> ");
            String input = scan.nextLine();

            // 뒤로가기 처리
            if (input.equals("0")) {
                System.out.println("구매를 취소했습니다.");
                System.out.println("----------------------------------------");
                return;
            }

            // 정수인지 검증 (공백, 음수, 소수, 문자, 특수기호 등은 차단)
            if (!input.matches("^[1-9][0-9]*$")) {
                System.out.println("상품 리스트에 존재하는 인덱스 숫자를 입력해주세요.");
                continue;
            }

            int select;
            try {
                select = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("상품 리스트에 존재하는 인덱스 숫자를 입력해주세요.");
                continue;
            }

            // 인덱스 범위 검사
            if (select < 1 || select > itemList.size()) {
                System.out.println("상품 리스트에 존재하는 인덱스 숫자를 입력해주세요.");
                continue;
            }

            Item selected = itemList.get(select - 1);

            // 판매 완료된 상품
            if (selected.status == 1) {
                System.out.println("이미 판매 완료된 상품입니다.");
                continue;
            }

            // 다른 사용자가 찜한 상품
            if (selected.dibID != null && !selected.dibID.isEmpty() &&
                    !selected.dibID.equals(currentUserInfo.ID)) {
                System.out.println("이미 다른 고객님이 찜한 상품입니다. 다른 상품을 선택해주세요.");
                continue;
            }

            // 정상 구매 처리
            selected.status = 1;
            currentUserInfo.buy.add(selected.itemNum);

            // 판매자 찾기
            User seller = null;
            for (User u : userList) {
                if (u.sell != null && u.sell.contains(selected.itemNum)) {
                    seller = u;
                    break;
                }
            }

            if (seller != null) seller.sell.remove((Integer) selected.itemNum);

            appendTransaction(currentUserInfo.ID, selected.itemNum, seller != null ? seller.ID : "Unknown");
            fileSaver();

            System.out.println("구매 완료!");
            System.out.println("----------------------------------------");

            trustEvaluation(seller, currentUserInfo);
            break;
        }
    }

    private void appendTransaction(String buyerID, int itemNum, String sellerID) {
        int transactionId = getNextTransactionId();
        // 1) 파일에 기록
        try (PrintWriter out = new PrintWriter(
                new BufferedWriter(new FileWriter("TransactionFile.txt", true)))) {
            out.println(
                    transactionId + "," +
                            sellerID + "," +
                            itemNum + ",0,0," +
                            buyerID + "," +
                            itemNum + ",0,0"
            );
        } catch (IOException e) {
            System.err.println("거래 기록 저장 실패: " + e.getMessage());
        }

        // 2) 메모리상의 tradeList 에도 추가 (플래그는 기본 false)
        TradeHistory newTh = new TradeHistory(transactionId, sellerID, buyerID, itemNum);
        tradeList.add(newTh);
    }


    private int getNextTransactionId() {
        int maxId = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader("TransactionFile.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 0) {
                    int id = Integer.parseInt(parts[0]);
                    if (id >= maxId) maxId = id + 1;
                }
            }
        } catch (IOException e) {
            return 0;
        }
        return maxId;
    }

    private void trustEvaluation(User seller, User currentUserInfo) {
        while (true) {
            System.out.println("이 상품을 구매하면서 판매한 사용자의 신뢰도를 평가해주세요. (1 - 5사이의 정수값, 신뢰도 평가 넘기기는 0 입력)");
            String input = scan.nextLine();
            if (isInteger(input)) {
                if (Integer.parseInt(input) <= 5 && Integer.parseInt(input) >= 1) {
                    seller.trustEvaluation =
                            (seller.trustEvaluation * seller.evaluationCount + Integer.parseInt(input))
                                    / (seller.evaluationCount + 1);
                    seller.evaluationCount++;
                    //seller의 badge와 memberGrade를 재검한 후 저장한다.
                    File file = new File("UserFile.txt");
                    StringBuilder updatedContent = new StringBuilder();
                    try (Scanner scan = new Scanner(file)) {
                        while (scan.hasNextLine()) {
                            String line = scan.nextLine();
                            String[] parts = line.split(",", 7);
                            if (!parts[0].equals(seller.ID) || !parts[1].equals(seller.ID)) {
                                continue;
                            }
                            if (parts.length >= 6) {
                                String grade = "";
                                if (seller.badge.equals("GOLD") && seller.reportScore == 0 && seller.trustEvaluation >= 4.5) {
                                    grade = "VVIP";
                                } else if (seller.badge.equals("GOLD") && seller.reportScore >= 1 &&
                                        seller.reportScore <= 4 && seller.trustEvaluation >= 4.5) {
                                    grade = "VIP";
                                } else if ((seller.badge.equals("SILVER") || seller.badge.equals("GOLD"))
                                        && seller.reportScore >= 5 && seller.reportScore <= 9 && seller.trustEvaluation >= 3) {
                                    grade = "COMMON";
                                } else if (seller.reportScore >= 10 && seller.trustEvaluation < 3) {
                                    grade = "DANGER";
                                }
                                parts[4] = grade;
                                String updatedLine = String.join(",", parts);
                                updatedContent.append(updatedLine).append("\n");
                            } else {
                                // 필드 수가 적을 경우 원본 유지 예외 처리
                                updatedContent.append(line).append("\n");
                            }
                        }
                        // 파일에 덮어쓰기
                        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                            writer.write(updatedContent.toString());
                        }
                        System.out.println("회원 등급이 성공적으로 업데이트되었습니다.");

                    } catch (IOException e) {
                        System.err.println("파일 처리 중 오류 발생: " + e.getMessage());
                    }
                    break;
                } else if (Integer.parseInt(input) == 0) {
                    System.out.println("사용자의 신뢰도 평가를 넘기셨습니다. 메인 메뉴로 돌아갑니다.");
                    break;
                } else {
                    System.out.println("잘못된 입력입니다. 다시 시도해 주세요.");
                }
            }
        }
        afterLogin(currentUserInfo);
    }

    private static boolean isInteger(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    //상품 등록 메서드 작성자 : 강현우
    private void registerMenu(User currentUserInfo) {
        while (true) {
            System.out.println("1. 상품 신규 등록 / 2. 기존 상품 수정 / 3. 기존 상품 삭제");
            System.out.println("이용할 메뉴의 번호를 입력하세요. (뒤로가기는 0을 입력하세요.)");
            String option = scan.nextLine();
            if (isInteger(option)) {
                int select = Integer.parseInt(option);
                if (select == 1) {
                    registerNewItem(currentUserInfo);
                    break;
                } else if (select == 2) {
                    reviseItem(currentUserInfo);
                    break;
                } else if (select == 3) {
                    deleteItem(currentUserInfo);
                    break;
                } else if (select == 0) {
                    break;
                } else {
                    System.out.println("메뉴의 번호가 올바르지 않습니다.");
                }
            } else {
                System.out.println("메뉴의 번호가 올바르지 않습니다.");
            }
        }
    }
    //상품 등록 메소드 : 등록자, 상품 명 인자 필요 / 작성자 : 구교영 / 2차 수정본 작성자 : 강현우
    //상품 등록 선택 시 추가 -> ProductRegistration.productRegister(유저);

    //6.2.4.1.1 카테고리 추가 기능을 여기에 추가로 만드시면 될 것 같습니다.
    private void registerNewItem(User user) {
        if (user.memberGrade.equals("DANGER")){
            System.out.println("상품 등록이 정지된 사용자입니다");
            return;
        }
        System.out.println("등록하실 상품의 정보를 입력해주세요");
        System.out.println("입력예시 : 상품명-가격");
        System.out.println("각 요소 사이는 '-'로 구분해주세요");
        System.out.println("----------------------------------------");
        System.out.print("-->>> ");
        String input = scan.nextLine();

        //String-String 형식인가?
        String[] elements = input.split("-");
        if (elements.length != 2 || elements[0].trim().isEmpty()) {
            System.out.println("입력 형식이 올바르지 않습니다");
            System.out.println("----------------------------------------");
            return;
        }
        //두 번째 문자열이 숫자인가?
        try {
            elements[1] = elements[1].replace(",", "");
//			int price = Integer.parseInt(elements[1]);
            BigInteger price = new BigInteger(elements[1]);
            if (price.compareTo(BigInteger.ZERO) == 0) {
                System.out.println("입력 형식이 올바르지 않습니다");
                System.out.println("----------------------------------------");
                return;
            }
//			Item item = new Item(elements[0], price, 0);
            Item item = new Item(++productNumMax, elements[0], price.toString(), "", currentUserInfo.ID, 0);
            user.sell.add(item.itemNum);
            itemList.add(item);
            fileSaver();

            if (item != null) {
                addCategory(extractCategoriesFromProducts("ProductFile.txt"), item, user);
            }

//            //			파일 수정
//            //기존에는 여기에서 바로 DataFile에다 업데이트 했었는데,
//        //전역 변수에 User랑 Item들 변경사항 저장해 두고 나서 프로그램 종료 누르면 전역 변수의 값들을 UserFile이랑 ItemFile에다 업데이트 하는 방법이 더 편할 거 같아요!
//        //제가 종료할 때 업데이트 파트는 구현해놓을게요!!
//            try {
//                //txt 임시 저장 배열
//                List<String> lines = new ArrayList<>();
//                String newLine;//수정할 한 줄을 넣을 변수
//                File file = new File("DataFile.txt");
//                Scanner fscan = new Scanner(file);
//                while (fscan.hasNextLine()) {
//                    String line = fscan.nextLine();
//                    String[] colon = line.split("\t");
//                    if (colon[0].equals(user.ID)) {
//                        //						라인 작성 과정
//                        newLine = user.ID + "\t" + user.password + "\t";
//                        //						gildong123	gildong123!	_
//                        for (Item item1 : user.sell)
//                            newLine += item1.name + "," + item1.price + "," + item1.status + "\t";
//                        //						gildong123	gildong123!	애플노트북,150000,0	<등록한 물품>	_
//                        newLine += ";\t";
//                        //						gildong123	gildong123!	애플노트북,150000,0	<등록한 물품>	;_
//                        for (Item item1 : user.buy)
//                            newLine += item1.name + "," + item1.price + "," + item1.status + "\t";
//                        //						gildong123	gildong123!	애플노트북,150000,0	<등록한 물품>	;파란색니트,300000,1	_
//                        newLine += ";";
//                        //						gildong123	gildong123!	애플노트북,150000,0	<등록한 물품>	;파란색니트,300000,1	;_
//                        lines.add(newLine);
//
//                    } else lines.add(line);
//                }
//                PrintWriter writer = new PrintWriter(file);
//                for (String line : lines) {//파일 덮어쓰기
//                    writer.println(line);
//                }
//                writer.close();
//                System.out.println("등록되셨습니다");
//                System.out.println("----------------------------------------");
//            } catch (FileNotFoundException e) {//프로그램 실행 도중 사용자가 직접 택스트 파일을 삭제했을 때만 들어오는 게 정상인 구문
//                System.out.println("파일 데이터 손상.");
//            }

        } catch (NumberFormatException e) {//두 번째 문자열이 숫자가 아니면 진입
            System.out.println("입력 형식이 올바르지 않습니다");
            System.out.println("----------------------------------------");
        }


    }

    private String[] addCategory(String[] categoryList, Item registered, User currentUserInfo) {
        Scanner scanner = new Scanner(System.in);
//        ArrayList<String> categories = new ArrayList<>(Arrays.asList(categoryList)); // 실시간 복사
        String[] categories = categoryList;
        while (true) {
            System.out.println("카테고리를 추가하시겠습니까?");
            System.out.println("----------------------------------------");
            System.out.println("현재 존재하는 카테고리");
//            System.out.println(categories);
            if (categories.length == 0) {
                System.out.print("[]");
            } else {
                System.out.print("[");
                System.out.print(String.join(", ", categories));
                System.out.println("]");
            }
            System.out.println("----------------------------------------");
            System.out.println("존재하는 카테고리를 입력하거나,");
            System.out.println("새 카테고리를 입력해주세요.");
            System.out.println("카테고리 추가를 원하시지 않으시다면 0을 입력해주세요.");
            System.out.println("----------------------------------------");
            System.out.print("-->>> ");
            String input = scanner.nextLine().trim();

            // '0' 입력: 취소
            if (input.equals("0")) {
                System.out.println("카테고리 추가를 취소하였습니다.");
                afterLogin(currentUserInfo);
                return categoryList;
            }

            // 입력이 유효하지 않음: 빈 문자열, 공백 등
            if (input.isEmpty() || input.matches("\\s+") || input.equals("0")) {
                System.out.println("입력 형식이 올바르지 않습니다.");
                continue;
            }

//            // 기존 카테고리 재사용
//            if (categories.contains(input)) {
//                registered.category = input;
//                fileSaver();
//                System.out.println("추가되었습니다.");
//                afterLogin(currentUserInfo);
//                return categoryList;
//            }
//
//            // 새 카테고리 추가
//            categories.add(input);
//            registered.category = input;
//            fileSaver();
//            System.out.println("추가되었습니다.");
//            afterLogin(currentUserInfo);
//            return categories.toArray(new String[0]);

            registered.category = input;
            boolean exists = false;
            for (String s : categories) {
                if (s.equals(input)) {
                    exists = true;
                    break;
                }
            }
            // 새 카테고리 추가
            if (!exists) {
                String[] newCategories = new String[categories.length + 1];
                System.arraycopy(categories, 0, newCategories, 0, categories.length);
                newCategories[categories.length] = input;
                categories = newCategories;
            }
            fileSaver();
            System.out.println("추가되었습니다.");
            afterLogin(currentUserInfo);
            return categories;
        }

    }

    private void reviseItem(User currentUserInfo) {
        ArrayList<Item> revisableItems = new ArrayList<>();
        ArrayList<Integer> revisableItemNums = new ArrayList<>();

        for (int itemNum : currentUserInfo.sell) {
            for (Item item : itemList) {
                if (item.itemNum == itemNum && item.status == 0 && (currentUserInfo.dib == null || !currentUserInfo.dib.contains(itemNum))) {
                    revisableItems.add(item);
                    revisableItemNums.add(item.itemNum);
                }
            }
        }

        if (revisableItems.isEmpty()) {
            System.out.println("수정 가능 상품이 존재하지 않습니다.");
            registerMenu(currentUserInfo);
            return;
        }

        System.out.println(currentUserInfo.ID + "님의 수정 가능 상품:");
        for (int i = 0; i < revisableItems.size(); i++) {
            Item item = revisableItems.get(i);
            System.out.print((i + 1) + ". " + item.name + ", " + item.price + "원");
            if (item.category != null && !item.category.isEmpty()) {
                System.out.print(", [" + item.category + "]");
            }
            System.out.println();
        }

        System.out.println("수정할 상품의 인덱스 번호를 입력하세요. 뒤로가기는 '0'");
        System.out.println("----------------------------------------");
        System.out.print("-->>> ");

        while (true) {
            String input = scan.nextLine();
            if (input.equals("0")) {
                registerMenu(currentUserInfo);
                return;
            }

            try {
                int select = Integer.parseInt(input);
                if (select >= 1 && select <= revisableItems.size()) {
                    int reviseItemNum = revisableItemNums.get(select - 1);
                    reviseInfoMenu(currentUserInfo, reviseItemNum);
                    return;
                }
            } catch (NumberFormatException ignored) {
            }

            System.out.println("상품 리스트에 존재하는 인덱스 숫자를 입력해주세요");
            System.out.print("-->>> ");
        }
    }

    private void reviseInfoMenu(User currentUserInfo, int reviseItemNum) {
        while (true) {
            System.out.println("1. 상품명 / 2. 가격 / 3. 카테고리 추가 / 4. 카테고리 삭제");
            System.out.println("수정하실 상품의 정보의 인덱스 번호를 입력하세요. 뒤로가기는 '0'");
            System.out.println("----------------------------------------");
            System.out.print("-->>> ");

            String input = scan.nextLine();
            switch (input) {
                case "1" -> {
                    reviseName(currentUserInfo, reviseItemNum);
                    return;
                }
                case "2" -> {
                    revisePrice(currentUserInfo, reviseItemNum);
                    return;
                }
                case "3" -> {
                    addCategoryInModifying(currentUserInfo, reviseItemNum);
                    return;
                }
                case "4" -> {
                    deleteCategory(currentUserInfo, reviseItemNum);
                    return;
                }
                case "0" -> {
                    reviseItem(currentUserInfo);
                    return;
                }
                default -> System.out.println("메뉴의 번호가 올바르지 않습니다.");
            }
        }
    }

    private String[] deleteCategory(User currentUserInfo, int reviseItemNum) {
        Item targetItem = null;
        for (Item item : itemList) {
            if (item.itemNum == reviseItemNum) {
                targetItem = item;
                break;
            }
        }

        if (targetItem == null) {
            System.out.println("해당 상품을 찾을 수 없습니다.");
            reviseInfoMenu(currentUserInfo, reviseItemNum);
            return extractCategoriesFromProducts("ProductFile.txt");
        }

        if (targetItem.category == null || targetItem.category.trim().isEmpty()) {
            System.out.println("상품에 존재하는 카테고리가 없습니다.");
            reviseInfoMenu(currentUserInfo, reviseItemNum);
            return extractCategoriesFromProducts("ProductFile.txt");
        }

        targetItem.category = "";
        fileSaver();
        System.out.println("카테고리 삭제 완료!");
        reviseInfoMenu(currentUserInfo, reviseItemNum);
        return extractCategoriesFromProducts("ProductFile.txt");
    }

    private String[] addCategoryInModifying(User currentUserInfo, int reviseItemNum) {
        Item targetItem = null;
        String[] categories = extractCategoriesFromProducts("ProductFile.txt");
        for (Item item : itemList) {
            if (item.itemNum == reviseItemNum) {
                targetItem = item;
                break;
            }
        }

        if (targetItem == null) {
            System.out.println("해당 상품을 찾을 수 없습니다.");
            reviseInfoMenu(currentUserInfo, reviseItemNum);
            return categories;
        }

        if (targetItem.category == null || targetItem.category.trim().isEmpty()) {
            while (true) {
                System.out.println("현재 존재하는 카테고리");
                if (categories.length == 0) {
                    System.out.print("[]");
                } else {
                    System.out.print("[");
                    System.out.print(String.join(", ", categories));
                    System.out.println("]");
                }
                System.out.println("----------------------------------------");
                System.out.println("존재하는 카테고리를 입력하거나, \n새 카테고리를 입력해주세요.");
                System.out.println("카테고리 추가를 원하시지 않으시다면 0을 입력해주세요.");

                System.out.print("-->>> ");

                String input = scan.nextLine();

                if (input.equals("0")) {
                    System.out.println("카테고리 추가를 건너뜁니다.");
                    reviseInfoMenu(currentUserInfo, reviseItemNum);
                    return categories;
                }

                if (input.isEmpty() || input.trim().isEmpty() || !input.equals(input.trim())) {
                    System.out.println("입력 형식이 올바르지 않습니다.");
                    System.out.println("----------------------------------------");
                    continue;
                }
                targetItem.category = input;
                System.out.println("추가되었습니다.");

                boolean exists = false;
                for (String s : categories) {
                    if (s.equals(input)) {
                        exists = true;
                        break;
                    }
                }

                //새 카테고리 추가
                if (!exists) {
                    String[] newCategories = new String[categories.length + 1];
                    System.arraycopy(categories, 0, newCategories, 0, categories.length);
                    newCategories[categories.length] = input;
                    categories = newCategories;
                }

                System.out.println("추가되었습니다.");
                try (PrintWriter productWriter = new PrintWriter(new FileWriter("ProductFile.txt"))) {
                    for (Item item : itemList) {
                        productWriter.println(
                                item.itemNum + "," +
                                        item.name + "," +
                                        item.price + "," +
                                        (item.category != null ? item.category : "") + "," +
                                        (item.dibID != null ? item.dibID : "") + "," +
                                        item.status
                        );
                    }
                } catch (IOException e) {
                    System.out.println("상품 파일 저장 중 오류 발생: " + e.getMessage());
                }
                reviseInfoMenu(currentUserInfo, reviseItemNum);
                return categories;
            }
        } else {
            System.out.println("이미 존재하는 카테고리가 있습니다.");
            reviseInfoMenu(currentUserInfo, reviseItemNum);
            return categories;
        }
    }

    private void reviseName(User currentUserInfo, int reviseItemNum) {
        System.out.println("상품명의 수정 정보를 입력하세요.");
        System.out.println("----------------------------------------");
        System.out.print("-->>> ");
        String input = scan.nextLine().trim();

        if (input.isEmpty() || input.contains("-")) {
            System.out.println("입력 형식이 올바르지 않습니다.");
            reviseInfoMenu(currentUserInfo, reviseItemNum);
            return;
        }

        for (Item item : itemList) {
            if (item.itemNum == reviseItemNum) {
                item.name = input;
                break;
            }
        }

        // 파일 반영 (ProductFile.txt)
        fileSaver(); // 이미 존재하는 저장 메서드 사용
        System.out.println("수정 완료!");
        reviseInfoMenu(currentUserInfo, reviseItemNum);
    }

    private void revisePrice(User currentUserInfo, int reviseItemNum) {
        System.out.println("가격의 수정 정보를 입력하세요.");
        System.out.println("----------------------------------------");
        System.out.print("-->>> ");
        String input = scan.nextLine().trim();

        if (input.isEmpty() || !input.replace(",", "").matches("\\d+")) {
            System.out.println("입력 형식이 올바르지 않습니다.");
            reviseInfoMenu(currentUserInfo, reviseItemNum);
            return;
        }

        for (Item item : itemList) {
            if (item.itemNum == reviseItemNum) {
                item.price = input;
                break;
            }
        }

        fileSaver();
        System.out.println("수정 완료!");
        reviseInfoMenu(currentUserInfo, reviseItemNum);
    }


    private void saveAllItemsToFile() {
        try (PrintWriter writer = new PrintWriter("ProductFile.txt")) {
            for (Item item : itemList) {
                writer.println(item.itemNum + "," + item.name + "," + item.price + "," + item.category + "," + item.dibID + "," + item.status);
            }
        } catch (IOException e) {
            System.out.println("상품 파일 저장 중 오류 발생: " + e.getMessage());
        }
    }

    private void deleteItem(User currentUserInfo) {
        ArrayList<Item> deletableItems = new ArrayList<>();
        ArrayList<Integer> deletableItemNums = new ArrayList<>();

        for (int itemNum : currentUserInfo.sell) {
            for (Item item : itemList) {
                if (item.itemNum == itemNum && item.status == 0) {
                    deletableItems.add(item);
                    deletableItemNums.add(item.itemNum);
                    break;
                }
            }
        }

        if (deletableItems.isEmpty()) {
            System.out.println("미거래 등록 상품이 존재하지 않습니다.");
            afterLogin(currentUserInfo);
            return;
        }

        System.out.println(currentUserInfo.ID + "님의 미거래 등록 상품:");
        for (int i = 0; i < deletableItems.size(); i++) {
            Item item = deletableItems.get(i);
            System.out.printf("%d. %s, %s원", i + 1, item.name, item.price);
            if (item.category != null && !item.category.isEmpty()) {
                System.out.printf(", [%s]", item.category);
            }
            System.out.println();
        }

        System.out.println("삭제할 상품의 인덱스 번호를 입력하세요. 뒤로가기는 '0'");
        System.out.println("----------------------------------------");
        System.out.print("-->>> ");

        while (true) {
            String input = scan.nextLine().trim();

            if (input.equals("0")) {
                registerMenu(currentUserInfo);
                return;
            }

            if (!input.matches("\\d+")) {
                System.out.println("상품 리스트에 존재하는 인덱스 숫자를 입력해주세요.");
                System.out.print("-->>> ");
                continue;
            }

            int index = Integer.parseInt(input);
            if (index < 1 || index > deletableItems.size()) {
                System.out.println("상품 리스트에 존재하는 인덱스 숫자를 입력해주세요.");
                System.out.print("-->>> ");
                continue;
            }

            Item toDelete = deletableItems.get(index - 1);
            int deleteItemNum = toDelete.itemNum;

            itemList.removeIf(i -> i.itemNum == deleteItemNum);
            currentUserInfo.sell.removeIf(num -> num == deleteItemNum);

            fileSaver(); // 유저 sell 목록 반영

            System.out.println("삭제 완료!");
            afterLogin(currentUserInfo);
            return;
        }
    }

    //flieSaver로 통합
//    private void saveAllUsersToFile() {
//        try (PrintWriter userWriter = new PrintWriter(new FileWriter("UserFile.txt"))) {
//            for (User user : userList) {
//                StringBuilder sb = new StringBuilder();
//                sb.append(user.ID).append(",")
//                        .append(user.password).append(",")
//                        .append(user.trustEvaluation).append(",")
//                        .append(user.evaluationCount).append(",")
//                        .append(user.badge).append(",")
//                        .append(user.memberGrade != null ? user.memberGrade : "").append(",")
//                        .append(user.reportScore).append(",");
//
//                sb.append("\t");
//                if (user.sell != null && !user.sell.isEmpty()) {
//                    for (int num : user.sell) sb.append(num).append("\t");
//                }
//                sb.append(";\t");
//
//                if (user.buy != null && !user.buy.isEmpty()) {
//                    for (int num : user.buy) sb.append(num).append("\t");
//                }
//                sb.append(";\t");
//
//                if (user.dib != null && !user.dib.isEmpty()) {
//                    for (int num : user.dib) sb.append(num).append("\t");
//                }
//
//                userWriter.println(sb);
//            }
//        } catch (IOException e) {
//            System.out.println("UserFile 저장 중 오류 발생: " + e.getMessage());
//        }
//    }


    //거래 내역 메소드 : 유저 정보 인자 필요/ 작성자 : 구교영
    //거래 내역 선택 시 추가 -> TradeHistory.tradeHistory(유저);

    /**
     * 거래 내역 조회 및 신고/좋아요 처리
     */
    public void tradeHistory(User user) {
        System.out.println("----------------------------------------");
        List<TradeHistory> myTrades = TradeHistory.getTradesByUser(tradeList, user);

        if (myTrades.isEmpty()) {
            System.out.println("거래 내역이 존재하지 않습니다.");
            return;
        }

        int index = 1;
        System.out.println("구매한 물품 :");
        for (TradeHistory th : myTrades) {
            if (th.getBuyerId().equals(user.getId())) {
                Item itm = findItemByNum(th.getItemNum());
                System.out.printf("%d. %s, %s원%n", index++, itm.name, itm.price);
            }
        }
        System.out.println("판매한 물품 :");
        for (TradeHistory th : myTrades) {
            if (th.getSellerId().equals(user.getId())) {
                Item itm = findItemByNum(th.getItemNum());
                System.out.printf("%d. %s, %s원%n", index++, itm.name, itm.price);
            }
        }

        System.out.println("\nS-번호 신고  |  L-번호 좋아요  |  0 뒤로가기");
        System.out.print("-->>> ");
        String fb = scan.nextLine().trim();
        if (!"0".equals(fb)) {
            // 설계문서에 따른 중앙 로직 호출
            TradeHistory.processReportOrLike(fb, user, tradeList, userList);
        }
    }

    // (헬퍼) itemNum 으로 Item 객체 찾기
    private Item findItemByNum(int itemNum) {
        for (Item itm : itemList) {
            if (itm.itemNum == itemNum) return itm;
        }
        return null;
    }


    // --- 회원 등급 확인 메뉴 ---
    // --- 회원 등급 확인 메뉴 ---
    private void gradeMenu(User currentUserInfo) {
        System.out.println("1. 회원 등급 확인  / 0: 뒤로 가기");
        System.out.print("-->>> ");
        String selRaw = scan.nextLine();          // trim() 제거하고 원본 그대로 읽기
        // 오직 "0" 또는 "1"만 허용
        if (!selRaw.matches("[01]")) {
            System.out.println("메뉴의 번호가 올바르지 않습니다. 다시 입력해주세요.");
            return;
        }
        if ("0".equals(selRaw)) {
            return;
        }

        // "1"인 경우에만 ID 입력
        System.out.print("확인할 사용자 ID를 입력하세요: ");
        String idRaw = scan.nextLine();           // trim() 제거
        // 공백이 포함된 ID 차단
        if (!idRaw.matches("\\S+")) {
            System.out.println("올바른 사용자 ID를 입력해주세요.");
            return;
        }
        // 공백 없는 ID만 사용
        showMemberGrade(idRaw);
    }

    private void showMemberGrade(String id) {
        for (User u : userList) {
            if (u.ID.equals(id)) {
                u.updateGradeAndPermission();
                System.out.printf("  신뢰도: %.1f%n", u.trustEvaluation);
                System.out.printf("  뱃지: %s%n", u.badge);
                System.out.printf("  누적 신고 횟수: %d%n", u.reportScore);
                System.out.printf("  회원 등급: %s%n", u.memberGrade);
                if (!u.canRegister()) {
                    System.out.println("  상품 등록 권한이 차단된 상태입니다.");
                }
                return;
            }
        }
        System.out.println("올바른 사용자 ID를 입력해주세요.");
    }


    //main() ------------------------------------------------------------------------------
    //start.run 실행 filename을 인자로 전달
    public static void main(String[] args) {
        DangKon start = new DangKon();
        start.run("UserFile.txt", "ProductFile.txt", "TransactionFile.txt");
    }
}