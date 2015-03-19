# 项目模块概览 #

现在rapid主要分为三大块:

  1. **rapid-core**          rapid的核心jar包,包含一些rapid自己对其它框架的扩展
  1. **rapid-generator**     代码生成器
  1. **rapid-archetype**     项目脚手架(暂时规划中)

rapid-generator又包含以下模块:
  1. **rapid-generator**      生成器引擎
  1. **rapid-generator-ext**  生成器引擎扩展,一些不适合放入生成器核心功能的东西放在这里
  1. **rapid-generator-cmd-line** 生成器的命令行,提供一些便捷的命令生成代码
  1. **rapid-generator-template** 生成器模板工程,包括相关模板,用于生成代码使用.