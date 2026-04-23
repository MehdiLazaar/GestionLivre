import http from 'k6/http';
import { check, sleep } from 'k6';

const baseUrl = 'http://localhost:8080';

export default function () {
  if (Math.random() < 0.5) {
    const payload = JSON.stringify({
      titre: `Livre-${Math.floor(Math.random() * 100000)}`,
      auteur: `Auteur-${Math.floor(Math.random() * 100000)}`
    });

    const res = http.post(`${baseUrl}/books`, payload, {
      headers: { 'Content-Type': 'application/json' },
    });

    check(res, {
      'POST status is 201': (r) => r.status === 201,
    });
  } else {
    const res = http.get(`${baseUrl}/books`);

    check(res, {
      'GET status is 200': (r) => r.status === 200,
    });
  }

  sleep(1);
}