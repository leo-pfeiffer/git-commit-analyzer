# Git Commit Analyzer

[![Netlify Status](https://api.netlify.com/api/v1/badges/bb8725e1-c3c8-49d1-8c47-2b4c0f2c10e0/deploy-status)](https://app.netlify.com/sites/git-commit-analyzer/deploys)

Git Commit Analyzer is a visual analytics tool for your Git commit history.

- Plot your commits, deletions, and additions
- Group by user or time
- Works with local git repositories
- Integrates with GitHub

Try it out [here.](https://git-commit-analyzer.netlify.app/)

## Project setup

Git Commit Analyzer uses [Pizzly](https://github.com/Bearer/Pizzly) as an OAuth proxy server for the GitHub integration. You will need to deploy your own instance of Pizzly (e.g. on Heroku) in order to run Git Commit Analyzer. This only takes a few minutes and a tutorial is provided [here](https://github.com/Bearer/Pizzly#getting-started).

To configure Git Commit Analyzer to use your own Pizzly instance, you will need to add two environmant variables.
Place a `.env` file in the root of the project with the following variables specified:

```md
VUE_APP_PIZZLY_HOST=***
VUE_APP_PIZZLY_SECRET_KEY=***
```

`VUE_APP_PIZZLY_HOST` is the host on which your Pizzly instance is deployed (`https://<my-pizzly-instance>.herokuapp.com`).

`VUE_APP_PIZZLY_SECRET_KEY` is a secret key that you can define to restrict access to your Pizzly instance ([see here](https://github.com/Bearer/Pizzly/blob/master/docs/securing-your-instance.md)).

Once you have done this, you can run the Vue app as usual:

```bash
npm install
npm run serve
```

## Feedback/Bugs

Please feel free to reach out with feedback! If you find any bugs, problems, or have other suggestions, simply submit an [Issue](https://github.com/leo-pfeiffer/git-commit-analyzer/issues).
