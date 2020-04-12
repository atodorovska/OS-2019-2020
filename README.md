# OS-2019-2020
Пример програми


## Networking

### TCP
Програмата се наоѓа во `mk.ukim.finki.os.networking.tcp`.
Функционира на следниов начин:
- Стартува `Server` на `localhost` порта `8000`
- Стартува 10 тест `Client` кои праќаат по едно барање
- За секое барање од клиентот, крева нов `Worker` thread и остава тој да го опслужи барањето, и повторно чека ново барање
- Секој `Worker` го чита барањето на корисникот, и враќа одговор
- Секој `Client` откако ќе прати, чека одговор и го принта на екран
- Потоа, имаме можност ние да генерираме наше барање со наши параметри


#### Telnet Test

Можеме да генерираме барање до серверот и од друга програма.
Имено, може да ја искористиме `telnet` алатката да ни воспостави TCP конекција со серверот, а потоа да го напишеме нашето барање.

Пример:

```bash
$ telnet localhost 8000
Trying ::1...
Connected to localhost.
Escape character is '^]'.
GET /Movies/Groundhog+Day
USER: Sasho Najdov (snajdov)

Hello, Sasho Najdov (snajdov)!
You requested to GET the resource: /Movies/Groundhog+Day

Connection closed by foreign host.
```