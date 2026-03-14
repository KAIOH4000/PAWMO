module.exports = {
  root: true,
  env: {
    node: true
  },
  extends: [
    'plugin:vue/essential',
    'eslint:recommended'
  ],
  parserOptions: {
    parser: 'babel-eslint'
  },
  rules: {
    'no-console': process.env.NODE_ENV === 'production' ? 'warn' : 'off',
    'no-debugger': process.env.NODE_ENV === 'production' ? 'warn' : 'off',
    'no-unused-vars': 'warn', // 启用警告
    'no-prototype-builtins': 'error', // 启用检查
    'no-undef': 'error', // 启用检查
    'eqeqeq': 'warn', // 使用严格相等
    'curly': 'warn', // if/for/while 语句使用大括号
    'no-empty': 'error', // 禁止空块语句
    'no-extra-semi': 'error', // 禁止多余的分号
    'no-irregular-whitespace': 'error', // 禁止不规则空白
    'no-multi-spaces': 'warn', // 禁止多个空格
    'no-multiple-empty-lines': ['warn', { 'max': 2 }] // 禁止多个空行
  }
}
