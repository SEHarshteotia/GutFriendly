import {
  Apple,
  BookOpenText,
  HeartPulse,
  Salad,
  ShieldAlert,
  Sparkles,
} from "lucide-react";

const healthArticles = [
  {
    icon: ShieldAlert,
    category: "Food safety",
    title: "How to identify stale or unsafe food",
    description:
      "Learn the common warning signs related to smell, texture, storage and packaging before eating.",
  },
  {
    icon: Salad,
    category: "Healthy eating",
    title: "Build a balanced everyday meal",
    description:
      "Use a simple mix of protein, fibre, vegetables and healthy carbohydrates for more balanced meals.",
  },
  {
    icon: HeartPulse,
    category: "Gut health",
    title: "Daily habits that support digestion",
    description:
      "Hydration, meal timing, movement and fibre can all contribute to better digestive health.",
  },
  {
    icon: Apple,
    category: "Nutrition",
    title: "Choosing better food while ordering",
    description:
      "Compare preparation methods, ingredients and portion sizes before selecting your meal.",
  },
  {
    icon: BookOpenText,
    category: "Awareness",
    title: "Understanding food hygiene ratings",
    description:
      "Know what cleanliness, storage and staff-hygiene checks can tell you about a food establishment.",
  },
  {
    icon: Sparkles,
    category: "Smart choices",
    title: "Safer street-food habits",
    description:
      "Observe cooking temperature, water use, cleanliness and ingredient storage before ordering.",
  },
];

function HealthHubPage() {
  return (
    <div className="health-hub-page section-container">
      <section className="health-hub-hero">
        <div>
          <p className="home-eyebrow">
            Learn and eat better
          </p>

          <h1>Health Hub</h1>

          <p>
            Practical information about food safety,
            nutrition, hygiene and everyday gut health.
          </p>
        </div>

        <div className="health-hub-hero-icon">
          <HeartPulse size={42} />
        </div>
      </section>

      <section className="health-feature-card">
        <div className="health-feature-icon">
          <ShieldAlert size={30} />
        </div>

        <div>
          <p className="home-eyebrow">
            Featured guidance
          </p>

          <h2>
            Make safer food choices before you order
          </h2>

          <p>
            Check hygiene indicators, verified reviews,
            inspection information and the GutTrust score
            instead of relying only on popularity.
          </p>
        </div>
      </section>

      <section className="health-articles-section">
        <div className="home-section-heading">
          <div>
            <p>Explore resources</p>
            <h2>Food and wellness guides</h2>
            <span>
              Quick educational reads for everyday use.
            </span>
          </div>
        </div>

        <div className="health-article-grid">
          {healthArticles.map((article) => {
            const Icon = article.icon;

            return (
              <article
                className="health-article-card"
                key={article.title}
              >
                <div className="health-article-icon">
                  <Icon size={25} />
                </div>

                <span>{article.category}</span>

                <h3>{article.title}</h3>

                <p>{article.description}</p>

                <button type="button">
                  Read guide
                </button>
              </article>
            );
          })}
        </div>
      </section>

      <section className="health-disclaimer">
        <ShieldAlert size={22} />

        <p>
          Health Hub content is educational and should
          not replace advice from a qualified medical or
          nutrition professional.
        </p>
      </section>
    </div>
  );
}

export default HealthHubPage;