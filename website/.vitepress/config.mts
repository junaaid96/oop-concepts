import { defineConfig } from 'vitepress';
import { withMermaid } from 'vitepress-plugin-mermaid';
import sidebarData from './sidebar.generated.json' with { type: 'json' };

const GH = 'https://github.com/junaaid96/oop-concepts';
const SITE = 'https://junaaid96.github.io/oop-concepts/';
const group = (text: string, from: number, to: number) => ({
  text,
  collapsed: false,
  items: sidebarData.topics.filter((t) => +t.num >= from && +t.num <= to).map(({ text, link }) => ({ text, link })),
});

export default withMermaid(defineConfig({
  srcDir: 'content',
  base: '/oop-concepts/',
  lang: 'en-US',
  title: 'OOP Concepts',
  titleTemplate: ':title · OOP Concepts',
  description: 'A hands-on guide to object-oriented programming with runnable Java 25 and C++20 examples, JUnit tests, interview questions, exercises, and OOP vs functional programming.',
  cleanUrls: true,
  ignoreDeadLinks: 'localhostLinks',
  lastUpdated: true,
  sitemap: { hostname: SITE },
  head: [
    ['meta', { name: 'theme-color', content: '#3c8772' }],
    ['meta', { property: 'og:type', content: 'website' }],
    ['meta', { property: 'og:site_name', content: 'OOP Concepts' }],
    ['meta', { name: 'twitter:card', content: 'summary' }],
  ],
  transformPageData(page) {
    const route = page.relativePath.replace(/(^|\/)index\.md$/, '$1').replace(/\.md$/, '');
    const title = page.frontmatter.title || page.title || 'OOP Concepts';
    const desc = page.frontmatter.description || page.description || '';
    page.frontmatter.head ??= [];
    page.frontmatter.head.push(
      ['link', { rel: 'canonical', href: SITE + route }],
      ['meta', { property: 'og:title', content: title }],
      ['meta', { property: 'og:url', content: SITE + route }],
      ['meta', { property: 'og:description', content: desc }],
    );
  },
  markdown: { lineNumbers: true },
  themeConfig: {
    nav: [
      { text: 'Learning path', link: '/' },
      { text: 'Basics', link: '/basics' },
      { text: 'Topics', link: '/topics/01_four_pillars' },
      { text: 'OOP vs FP', link: '/guides/oop-vs-functional' },
      { text: 'C++ vs Java', link: '/guides/cpp-vs-java' },
    ],
    sidebar: [
      { text: 'Foundations', collapsed: false, items: [{ text: 'The basics', link: '/basics' }] },
      group('Core OOP in depth', 1, 10),
      group('Design', 11, 15),
      { ...group('Paradigms', 16, 16), items: [...group('Paradigms', 16, 16).items, ...sidebarData.guides.filter((g) => !g.link.endsWith('contributing'))] },
      { text: 'Contributing', collapsed: true, items: sidebarData.guides.filter((g) => g.link.endsWith('contributing')) },
    ],
    socialLinks: [{ icon: 'github', link: GH }],
    search: { provider: 'local' },
    outline: { level: [2, 3] },
    editLink: {
      pattern: ({ frontmatter }) => `https://github.com/junaaid96/oop-concepts/blob/main/${frontmatter.source ?? ''}`,
      text: 'View source on GitHub',
    },
    footer: { message: 'Released under the MIT License.', copyright: '© 2026 Md. Junaidul Islam' },
  },
  mermaid: { theme: 'default' },
}));
