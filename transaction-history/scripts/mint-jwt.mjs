#!/usr/bin/env node
import { createSign, createPrivateKey } from "node:crypto";
import { readFileSync } from "node:fs";
import { fileURLToPath } from "node:url";
import { dirname, join } from "node:path";
const root = join(dirname(fileURLToPath(import.meta.url)), "..");
const acct = process.argv[2] || "1011226111";
const pem = readFileSync(join(root, "standins/keys/jwtRS256.key"), "utf8");
const b64url = (buf) => Buffer.from(buf).toString("base64url");
const header = b64url(JSON.stringify({ alg: "RS256", typ: "JWT" }));
const now = Math.floor(Date.now() / 1000);
const payload = b64url(JSON.stringify({ acct, iat: now, exp: now + 3600 }));
const data = `${header}.${payload}`;
const sign = createSign("RSA-SHA256"); sign.update(data); sign.end();
process.stdout.write(`${data}.${sign.sign(createPrivateKey(pem)).toString("base64url")}\n`);
