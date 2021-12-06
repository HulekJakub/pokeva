1. Zasady gry
    Każdy gracz otrzymuje 100 pieniędzy na start.
    Na początku rundy każdy z graczy jest zobowiązany zapłacić ante w wysokości 2.
    Pozostałe zasady są opisane na stronie
    https://terazpoker.pl/poker-pieciokartowy-dobierany-5-card-draw/
    Rundę wygrywa gracz, który ma rękę o największej wartości.
    Gracz który straci wszystkie swoje pieniądze zostaje wyeliminowany.
    Gra kończy się, gdy pozostanie jeden niewyeliminowany gracz.

2. Sposób uruchomienia programu
    Należy uruchomić aplikację ServerApp argumentem mówiącym o liczbie graczy,
      lub wprowadzić go po załączeniu aplikacji w przypadku braku wprowadzonego argumentu.
    Uruchomić odpowiednią liczbę aplikacji ClientApp i postępować zgodnie z instrukcjami

3. Protokół komunikacji
    Serwer wysyła do klientów ciągi znaków w formacie: "TYP_WIADOMOSCI$###$PARAMETR_WIADOMOSCI$###$TEKST_DO_WYSWIETLENIA",
        gdzie:
            -"TYP_WIADMOSCI" może przyjmować wartości ze zbioru {"LOGIN", "INFO", "GAME_MESSAGE"},
                gdzie:
                - "LOGIN" prosi o podanie nazwy gracza oraz przesyła nazwę gry - wymaga w odpowiedzi poprawnej nazwy
                - "INFO" podaje wiadomośc do wyświetlenia - wymaga tylko potwierdzenia otrzymania wiadmości
                - "GAME_MESSAGE" podaje wiadomość do wyświetlenia - wymaga odpowiedniej komendy z odpowiednim parametrem
            -"PARAMETR_WIADMOSCI" jest wykorzystywany przez Client tylko w przypadku wiadomosci typu "LOGIN", gdzie
              służy do przekazania nazwy gry, w innych przypadkach jest wyświetlany po "TEKST_DO_WYSWIETLENIA".

    Klient wysyła do serwera dane tylko jako odpowiedź na wiadomość serwera,
      wiadomości wysyłane przez klienta są w formacie "NAZWA_SERWERA NAZWA_GRACZA TYP_RUCHU PARAMETR", lub
      "NAZWA_SERWERA NAZWA_GRACZA CONFIRM", gdy serwer wymaga tylko potwierdzenia otrzymania wiadomości.
    "TYP_RUCHU" możę przyjmować wartości ze zbioru: {EXCHANGE, PASS, BET, CHECK, ALLIN},
        gdzie:
            - "EXCHANGE" służy do wymiany kart i parametr oznacza wtedy numery kart do wymiany;
            - "PASS" służy do spasowania, parametr jest pomijany;
            - "BET" służy do postawienia pieniędzy, paramter oznacza dodatnią liczbę pieniędzy;
                jeśli parametr jest mniejszy niż minimalny dopuszczalny zostanie on podwyższony do minimalnej wartości,
                a jeśli jest większy od posiadanych pieniędzy zostanie on pomniejszony o tą różnicę;
            - "CHECK" służy do postawienia minimalnej ilości pieniędzy, parametr jest pomijany;
            - "ALLIN" służy do postawienia wszystkich swoich pieniędzy

    Serwer podejmuje odpowiednie działania na podstawie otrzymanych "TYP_RUCHU" i "PARAMETR".
