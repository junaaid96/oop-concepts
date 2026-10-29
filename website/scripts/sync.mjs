// Generates website/content/ from the repo's Markdown (single source of truth).
// Never edit website/content by hand: it is wiped and rebuilt on every build.
const GH = 'https://github.com/junaaid96/oop-concepts';
const RAW = 'https://raw.githubusercontent.com/junaaid96/oop-concepts/main';
import fs from 'node:fs';
import path from 'node:path';
import { fileURLToPath } from 'node:url';

const ROOT = path.resolve(path.dirname(fileURLToPath(import.meta.url)), '../..');
const OUT = path.resolve(ROOT, 'website/content');
fs.rmSync(OUT, { recursive: true, force: true });
fs.mkdirSync(OUT, { recursive: true });

const rel = (p) => path.relative(ROOT, p).split(path.sep).join('/');
const read = (p) => fs.readFileSync(path.join(ROOT, p), 'utf8');
const pages = new Map(); // repo-relative source -> site route

function resolveLink(fromRel, href, isImage) {
  if (/^([a-z]+:|#|\/)/i.test(href)) return href;
  const [p, hash = ''] = href.split('#');
  const target = path.posix.normalize(path.posix.join(path.posix.dirname(fromRel), p)).replace(/\/$/, '');
  const h = hash ? `#${hash}` : '';
  if (isImage) return `${RAW}/${target}`;
  for (const cand of [target, `${target}/README.md`]) if (pages.has(cand)) return `/${pages.get(cand)}${h}`;
  if (target === 'README.md' || target === '.') return `/${h}`;
  const abs = path.join(ROOT, target);
  const kind = fs.existsSync(abs) && fs.statSync(abs).isDirectory() ? 'tree' : 'blob';
  return `${GH}/${kind}/main/${target}${h}`;
}

// Rewrite links outside fenced code blocks.
function rewrite(md, fromRel) {
  let inFence = false;
  return md.split('\n').map((line) => {
    if (/^\s*(```|~~~)/.test(line)) { inFence = !inFence; return line; }
    if (inFence) return line;
    return line.replace(/(!?)\[([^\]]*)\]\(([^)\s]+)\)/g, (_, bang, t, h) => `${bang}[${t}](${resolveLink(fromRel, h, !!bang)})`);
  }).join('\n');
}

const fm = (o) => `---\n${Object.entries(o).map(([k, v]) => `${k}: ${JSON.stringify(v)}`).join('\n')}\n---\n\n`;
const firstHeading = (md) => (md.match(/^#\s+(.+)$/m) || [, ''])[1].trim();
const plain = (s) => s.replace(/[*_`]/g, '').replace(/\[([^\]]*)\]\([^)]*\)/g, '$1');
const firstPara = (md) => {
  const para = md.split('\n\n').find((b) => b.trim() && !/^(#|>|\||```|!\[|\[!\[|-)/.test(b.trim()));
  return plain((para || '').replace(/\s+/g, ' ').trim()).slice(0, 180);
};

function walk(dir, exts) {
  const abs = path.join(ROOT, dir);
  if (!fs.existsSync(abs)) return [];
  return fs.readdirSync(abs, { withFileTypes: true }).sort((a, b) => a.name.localeCompare(b.name))
    .flatMap((e) => {
      const p = `${dir}/${e.name}`;
      if (e.isDirectory()) return walk(p, exts);
      return exts.some((x) => e.name.endsWith(x)) ? [p] : [];
    });
}

const LANG = { '.java': 'java', '.cpp': 'cpp', '.xml': 'xml', '.yaml': 'yaml', '.yml': 'yaml', '.properties': 'properties', '.sql': 'sql', '.txt': 'text' };
function sourceBlocks(files, intro) {
  if (!files.length) return '';
  let s = `\n\n## Source code\n\n${intro}\n`;
  for (const r of files) {
    const test = r.includes('/src/test/');
    const lang = LANG[path.extname(r)] || 'text';
    const badge = test ? '<Badge type="tip" text="test" />' : `<Badge type="info" text="${lang === 'cpp' ? 'C++' : lang === 'java' ? 'source' : lang}" />`;
    const code = read(r).replace(/\n$/, '');
    const fence = code.includes('```') ? '````' : '```';
    s += `\n::: details ${r.split('/').slice(-1)[0]} ${badge}\n[View on GitHub](${GH}/blob/main/${r})\n\n${fence}${lang}\n${code}\n${fence}\n:::\n`;
  }
  return s;
}

function emit(route, srcRel, body, extra = {}) {
  const file = path.join(OUT, route === '' ? 'index.md' : route.endsWith('/') ? `${route}index.md` : `${route}.md`);
  fs.mkdirSync(path.dirname(file), { recursive: true });
  fs.writeFileSync(file, fm({ source: srcRel, ...extra }) + body + '\n');
}

// Root README body without title, badges and its own "Website" section.
function readmeBody() {
  return read('README.md').split('\n').filter((l, i) => !(i === 0 && l.startsWith('# ')) && !/^\s*(\[!\[|!\[)/.test(l))
    .join('\n').replace(/^## Website[\s\S]*?(?=^## )/m, '');
}

const topics = fs.readdirSync(path.join(ROOT, 'topics')).filter((d) => /^\d\d_/.test(d)).sort();
for (const t of topics) pages.set(`topics/${t}/README.md`, `topics/${t}`);
pages.set('docs/OOP_VS_FUNCTIONAL.md', 'guides/oop-vs-functional');
pages.set('docs/CPP_VS_JAVA_OOP.md', 'guides/cpp-vs-java');
pages.set('CONTRIBUTING.md', 'guides/contributing');
pages.set('basics', 'basics'); // generated page, linked from the README's Stage 1

const sidebar = { topics: [], guides: [] };
const readme = readmeBody();

// Stage 1: a page built from the README's Stage 1 table plus every basics/ file.
{
  const stage = (readme.match(/^### Stage 1[\s\S]*?(?=^### )/m) || [''])[0].replace(/^### .*\n/, '');
  const files = [...walk('basics', ['.cpp']), ...walk('basics/src/main/java', ['.java']), ...walk('basics/src/test/java', ['.java'])];
  const md = `# Foundations: the basics\n\nFirst steps in OOP: classes, objects, methods, the kinds of inheritance, encapsulation, polymorphism and abstraction, each shown in C++ and in Java.\n${rewrite(stage, 'README.md')}\n> The original exercise brief is in [basics/0.00_task.txt](${GH}/blob/main/basics/0.00_task.txt).`;
  emit('basics', 'basics', md + sourceBlocks(files, `Every C++ file and the Java basics module, embedded from the repo. Browse the folder on [GitHub](${GH}/tree/main/basics).`),
    { title: 'Foundations: the basics', description: 'Classes, objects, methods, inheritance, encapsulation, polymorphism and abstraction in C++ and Java.' });
}

for (const t of topics) {
  const srcRel = `topics/${t}/README.md`;
  const md = read(srcRel);
  const title = plain(firstHeading(md));
  const files = [...walk(`topics/${t}`, ['.cpp']).filter((f) => !f.includes('/src/')), ...walk(`topics/${t}/src/main/java`, ['.java']), ...walk(`topics/${t}/src/test/java`, ['.java'])];
  const body = rewrite(md, srcRel) + sourceBlocks(files, `The C++ example, the Java code and its JUnit tests for this topic, embedded straight from the repo. Browse the folder on [GitHub](${GH}/tree/main/topics/${t}).`);
  emit(`topics/${t}`, srcRel, body, { title: title.replace(/^\d\d · /, ''), description: firstPara(md) });
  sidebar.topics.push({ num: t.slice(0, 2), text: title.replace(' · ', '. '), link: `/topics/${t}` });
}
for (const [srcRel, route] of pages) {
  if (!route.startsWith('guides/')) continue;
  const md = read(srcRel);
  emit(route, srcRel, rewrite(md, srcRel), { title: plain(firstHeading(md)), description: firstPara(md) });
  sidebar.guides.push({ text: plain(firstHeading(md)).replace(/ — .*/, ''), link: `/${route}` });
}

const home = fm({ layout: 'home', source: 'README.md', title: 'OOP Concepts in Java and C++', description: firstPara(readme) })
  .replace(/---\n$/, `hero:
  name: "OOP Concepts"
  text: "Object-oriented programming in Java 25 and C++20"
  tagline: "From the four pillars to SOLID, design patterns and OOP vs functional. Every topic has a guide, runnable Java with JUnit tests, a C++ example, interview questions and exercises with solutions."
  actions:
    - theme: brand
      text: Start with the basics
      link: /basics
    - theme: alt
      text: C++ vs Java OOP
      link: /guides/cpp-vs-java
    - theme: alt
      text: View on GitHub
      link: ${GH}
features:
  - title: Foundations
    details: Classes, objects, methods, inheritance, encapsulation, polymorphism and abstraction side by side.
    link: /basics
  - title: Core OOP in depth
    details: Interfaces, composition, generics, records and sealed types, equality, lifecycle, exceptions, nested classes.
    link: /topics/01_four_pillars
  - title: Design
    details: SOLID, design by contract, dependency injection, design patterns and anti-patterns.
    link: /topics/11_solid
  - title: Paradigms
    details: OOP vs functional programming, and what C++ makes explicit that Java hides (virtual, override, RAII).
    link: /guides/oop-vs-functional
---
`);
fs.writeFileSync(path.join(OUT, 'index.md'), home + '\n' + rewrite(readme, 'README.md') + '\n');
fs.writeFileSync(path.resolve(ROOT, 'website/.vitepress/sidebar.generated.json'), JSON.stringify(sidebar, null, 2));
console.log(`synced ${topics.length} topics, ${sidebar.guides.length} guides, basics`);
